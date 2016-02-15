/* 
 * The MIT License
 *
 * Copyright 2016 Oskar Gusgård, Aleksi Rasio, Joel Vainikka, Joona Vainikka.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
var baseURL = "http://localhost:8080/RESTfulWebApp/webresources";

// development login flag, so that we are "logged in" as a specific user
var loggedUser = 0;

function getQueryVariable(variable) {
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}

function login() {
   
   var username = getQueryVariable("username");
   var password = getQueryVariable("password");
   
    if ((username === "user") && (password === "pass")) {
        loggedUser = 0;
    } else if ((username === "user1") && (password === "pass")) {
        loggedUser = 1;
    } else if ((username === "user2") && (password === "pass")) {
        loggedUser = 2;
    } else if ((username === "user3") && (password === "pass")) {
        loggedUser = 3;
    }
}

function toTime(s) {
    var myDate = new Date(s * 1);
    return myDate.toLocaleString();
}

// Fix so that the user doesn't show in the contacts
function listContacts(xml) {
    console.log('In listContacts');
    //var xmlString = (new XMLSerializer().serializeToString(xml));
    var $xml = $(xml);
    var $contactsContent = $('#contactsContent');

    $contactsContent.append($('<ul id="contactsList">\n\
                    </ul>'));
    var $contactsList = $("#contactsList");

    $xml.find('user').each(function () {
        if (parseInt($(this).find('userId').text()) !== loggedUser) {
            //console.log("UserID: " + $(this).find('userId').text());
            $contactsList.append('<li><button>' +
                    $(this).find('firstname').text() +
                    " " + $(this).find('lastname').text() + '</button></li>');
        }
    });
}

function getContacts() {
    console.log('In getContacts');
    $.ajax({
        url: baseURL + '/users',
        method: 'GET',
        dataType: 'xml',
        success: listContacts
    });
}

function listGroups(xml) {
    console.log('In listGroups');
    //var xmlString = (new XMLSerializer().serializeToString(xml));
    var $xml = $(xml);
    var $groupsContent = $('#groupsContent');

    $groupsContent.append($('<ul id="groupsList"></ul>'));
    var $groupsList = $("#groupsList");

    $xml.find('group').each(function () {
        // $groupsList.append('<li><form action="groupChat.html"><input type="hidden" name="" value="'+$(this).find('groupId').text()+'"/>'+ '<input type=submit value="'+ $(this).find('name').text()+'"/></form></li>');
        //$groupsList.append('<li><form><input type="hidden" name="" value="'+$(this).find('groupId').text()+'"/>'+ '<input id="group-chat-button" type=submit value="'+ $(this).find('name').text()+'"/></form></li>');
        $groupsList.append('<li><button id="group-chat-button">' + $(this).find('name').text() + '</button></li><button id="groupid-button">' + $(this).find('groupId').text() + '</button></li>');
    });
}

function getGroups() {
    console.log('In getGroup');
    $.ajax({
        url: baseURL + '/groups',
        method: 'GET',
        dataType: 'xml',
        success: listGroups
    });
}

function listMessages(xml) {
    console.log('In listMessages');
    //var xmlString = (new XMLSerializer().serializeToString(xml));
    var $xml = $(xml);
    var $messagesContainer = $('#messages');
    var messageBuffer = [];
    var promises = [];

    /*
     var messageCount = $xml.find('message').size();
     console.log('messagecount:' + messageCount);
     */

    $xml.find('message').each(function () {
        var $messageData = $(this);
        var uid = parseInt($messageData.find('fromUserId').text());
        var firstname = '', lastname = '';

        console.log('starting ajax call - promises size now: ' + promises.length);

        // push promise to array
        promises.push(
                $.ajax({
                    url: baseURL + '/users/' + uid,
                    method: 'GET',
                    dataType: 'xml',
                    success: function (userXml) {
                        firstname = $(userXml).find('firstname').text();
                        lastname = $(userXml).find('lastname').text();
                        var messageHTML = '';
                        var timestamp = 0;
                        // message was sent by me, style it accordingly
                        if (uid === loggedUser) {
                            messageHTML = messageHTML.concat('<div class="my-message">' +
                                    '<p class="message-body">' +
                                    $messageData.find('text').text() +
                                    '</p>' +
                                    '<p class="timestamp">' +
                                    toTime(timestamp = $messageData.find('timestamp').text()) +
                                    '</p>' +
                                    '</div>'
                                    );
                        } else {
                            // message was sent by someone else
                            messageHTML = messageHTML.concat('<div class="others-message">' +
                                    '<p class="username">' +
                                    firstname + ' ' +
                                    lastname +
                                    '</p>' +
                                    '<p class="message-body">' +
                                    $messageData.find('text').text() +
                                    '</p>' +
                                    '<p class="timestamp">' +
                                    toTime(timestamp = $messageData.find('timestamp').text()) +
                                    '</p>' +
                                    '</div>'
                                    );
                        }

                        // convert timestamp string to integer
                        timestamp = parseInt(timestamp);
                        messageBuffer.push({timestamp: timestamp, message: messageHTML});

                        console.log('pushed values ...');
                        console.log('timestamp: ' + timestamp);
                        console.log('message: ' + messageHTML);
                        console.log('buffer now: ' + messageBuffer.toString());
                    }
                }));
        console.log('promises size now: ' + promises.length);
        console.log('promises contains: ' + promises.toString());
    });

    // MESSAGE BUFFERING DONE

    // wait for ajax calls to complete
    $.when.apply($, promises).done(function () {

        // sort the buffer by timestamps
        messageBuffer.sort(function (x, y) {
            return x.timestamp - y.timestamp;
        });

        // clear the message container
        $messagesContainer.empty();

        // output newly-populated message buffer to the container
        for (var i = 0; i < messageBuffer.length; i++) {
            console.log('message in buffer: ' + messageBuffer[i].message.toString());
            $messagesContainer.append($.parseHTML((messageBuffer[i].message)));
        }

        // finally, scroll message container to bottom
        $messagesContainer.animate({scrollTop: $messagesContainer[0].scrollHeight}, 500);
    });

}

function sendMessage(message) {
    var xml = "<message><body><text></text></body><channel></channel><fromUserId></fromUserId><messageId>-1</messageId></message>";
    var xmlDoc = $.parseXML(xml);
    var $xml = $(xmlDoc);

    var serializer = new XMLSerializer();

    var debugXmlString = serializer.serializeToString($xml[0]);
    console.log("message to append is: " + message);
    console.log("debugXmlString is: " + debugXmlString);

    $.ajax({
        url: baseURL + '/users/' + loggedUser,
        method: 'GET',
        dataType: 'xml',
        success: function (xml) {

            // THIS IS JUST A QUICK REWRITE FOR NOW
            // we needlessly fetch the logged user's userid via ajax

            //console.log("xml received: " + serializer.serializeToString(xml));
            var fromUserXML = $(xml).find('fromUserId').clone();
            $xml.find('fromUserId').append(fromUserXML);
            //console.log("$xml after fromUser append: " + serializer.serializeToString($xml[0]));

            $xml.find('text').append(message);

            // CHANNEL_xxx STRING COULD BE A FUNCTION PARAMETER
            // it would enable us to use a single function for sending messages to all channels
            $xml.find('channel').append('CHANNEL_BROADCAST');
            //console.log("$xml after text and channel appends: " + serializer.serializeToString($xml[0]));
            xmlDoc = serializer.serializeToString($xml[0]);
        },
        complete: function () {
            $.ajax({
                url: baseURL + "/messages/add",
                data: xmlDoc,
                processData: false,
                type: 'POST',
                contentType: 'application/xml',
                success: function () {
                    // empty message-field
                    $('#message-field').val('');
                },
                error: function () {
                    alert('DEBUG: sendMessage AJAX error!');
                },
                complete: function () {
                    // update messages
                    getBroadcasts();
                }
            });
        }
    });
}

// this is redundant now
function getUserXml(user) {
    console.log("getUser user value to be fetched: " + user);
    //var user = user;
    var data = $.ajax({
        url: baseURL + '/users/' + user,
        method: 'GET',
        dataType: 'xml'
    });

    return data;
}

function getBroadcasts() {
    console.log('In getBroadcasts');
    $.ajax({
        url: baseURL + '/messages/broadcast',
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
}

// Work in progress. Trying to get the group backlog and show it on chat window
function getGroupMessages(groupid) {
    console.log('In getPrivate');
    $.ajax({
        url: baseURL + '/messages/' + groupid,
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
}

var tagBody = '(?:[^"\'>]|"[^"]*"|\'[^\']*\')*';

var tagOrComment = new RegExp(
        '<(?:'
        + '!--(?:(?:-*[^->])*--+|-?)'
        + '|script\\b' + tagBody + '>[\\s\\S]*?</script\\s*'
        + '|style\\b' + tagBody + '>[\\s\\S]*?</style\\s*'
        + '|/?[a-z]'
        + tagBody
        + ')>',
// global identifier without case sensitiviness
        'gi');

function validateInput(input) {
    var oldInput;
    do {
        oldInput = input;
        input = input.replace(tagOrComment, '');
    } while (input !== oldInput);
    console.log("RegEx is hard!");
    // "&lt" means "<" in ascii(replacing this prevents <scripts> from being run)
    return input.replace(/</g, '&lt;');
}

/* // old version not needed anymore, left for sake of backrolling
 function validateInput(input) {
 // returns true for valid text content (no empty string or just whitespace)
 var value = $.trim(input);
 
 if (value.length > 0) {
 return true;
 }
 
 return false;
 }
 */
