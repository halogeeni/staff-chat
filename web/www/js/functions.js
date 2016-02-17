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
var loggedUser = login();

// we need this for autoscrolling on new messages
var messageCount = 0;
var broadcastClicked = false;

function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return(false);
}

function login() {

    var username = getQueryVariable("username");
    var password = getQueryVariable("password");

    if ((username === "user0") && (password === "pass")) {
        console.log('user 0');
        return 0;
    } else if ((username === "user1") && (password === "pass")) {
        console.log('user 1');
        return 1;
    } else if ((username === "user2") && (password === "pass")) {
        console.log('user 2');
        return 2;
    } else if ((username === "user3") && (password === "pass")) {
        console.log('user 3');
        return 3;
    }
    // default to user zero if no parameters are passed
    return 0;
}

function toTime(s) {
    var myDate = new Date(s * 1);
    return myDate.toLocaleString();
}

function listContacts(xml) {
    var $xml = $(xml);
    var $contactsContent = $('#contactsContent');

    $contactsContent.append($('<form action="ContactsTest.html"><ul id="contactsList">\n\</ul></form>'));
    var $contactsList = $("#contactsList");

    $xml.find('user').each(function () {
        if (parseInt($(this).find('userId').text()) !== loggedUser) {
            //console.log("UserID: " + $(this).find('userId').text());
            $contactsList.append('<li><button value="' + $(this).find('userId').text() + '">' +
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
    var $xml = $(xml);
    var $groupsContent = $('#groupsContent');

    $groupsContent.append($('<form action="GroupsTest.html"><ul id="groupsList"></ul></form>'));
    var $groupsList = $("#groupsList");

    $xml.find('group').each(function () {
        // $groupsList.append('<li><form action="groupChat.html"><input type="hidden" name="" value="'+$(this).find('groupId').text()+'"/>'+ '<input type=submit value="'+ $(this).find('name').text()+'"/></form></li>');
        //$groupsList.append('<li><form><input type="hidden" name="" value="'+$(this).find('groupId').text()+'"/>'+ '<input id="group-chat-button" type=submit value="'+ $(this).find('name').text()+'"/></form></li>');
        $groupsList.append('<li><button value="' + $(this).find('ID').text() + '" '
                + 'id="group-chat-button">' + $(this).find('name').text() + '</button></li>');
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
    //console.log('In listMessages');
    var $xml = $(xml);
    var $messagesContainer = $('#messages');
    var messageBuffer = [];
    var promises = [];

    // get the current number of messages
    var currentMessageCount = $xml.find('message').size();

    // update message view only when new messages are available


    //Messages will only be shown when index.html is loaded or when a new message arrives
    if (currentMessageCount > messageCount || broadcastClicked === true) {
        broadcastClicked = false;

        $xml.find('message').each(function () {
            var $messageData = $(this);
            var uid = parseInt($messageData.find('fromUserId').text());
            var firstname = '', lastname = '';

            // push ajax call promise to array
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
                        }
                    }));
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
                //console.log('message in buffer: ' + messageBuffer[i].message.toString());
                $messagesContainer.append($.parseHTML((messageBuffer[i].message)));
            }

            // finally, scroll message container to bottom
            $messagesContainer.animate({scrollTop: $messagesContainer[0].scrollHeight}, 500);

        });

    }

    // update message counter
    messageCount = currentMessageCount;
}

function sendMessage(message) {
    var xml = "<message><body><text></text></body><channel></channel><fromUserId></fromUserId><messageId>-1</messageId></message>";
    var xmlDoc = $.parseXML(xml);
    var $xml = $(xmlDoc);
    var serializer = new XMLSerializer();

    $.ajax({
        url: baseURL + '/users/' + loggedUser,
        method: 'GET',
        dataType: 'xml',
        success: function (xml) {
            // append sender's user id to the xml
            $xml.find('fromUserId').append(loggedUser);
            // append message body (text)
            $xml.find('text').append(message);
            // CHANNEL_xxx STRING COULD BE A FUNCTION PARAMETER
            // it would enable us to use a single function for sending messages to all channels
            $xml.find('channel').append('CHANNEL_BROADCAST');
            // serialize the xml for sending
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
                }
            });
        }
    });
}

function getBroadcasts() {

    //console.log('In getBroadcasts');
    $.ajax({
        url: baseURL + '/messages/broadcast',
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
}

// Work in progress. Trying to get the group backlog and show it on chat window
function getGroupMessages(groupid) {
    //console.log('In getPrivate');
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

// Function to get user's firstname, lastname and title to navigation
function getUser() {
    $.ajax({
        url: baseURL + '/users/' + loggedUser,
        method: 'GET',
        dataType: 'xml',
        success: function (userXml) {
            firstname = $(userXml).find('firstname').text();
            lastname = $(userXml).find('lastname').text();

            var userHTML = '';

            // Title is the job title e.g. "Nurse"
            userHTML = userHTML.concat(
                    firstname + ' ' + lastname + '<br>' + "<i>job title</i>");

            $('#loggedInAs').append(userHTML);
        }
    });
}

function broadcastTrigger() {

    console.log("In broadcastClick");
    broadcastClicked = true;

}
