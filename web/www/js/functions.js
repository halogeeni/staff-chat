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

function toTime(s) {
    var myDate = new Date(s * 1);
    return myDate.toLocaleString();
}

// Fix so that the user doesn't show in the contacts
function listContacts(xml, status) {
    console.log('In listContacts');
    //var xmlString = (new XMLSerializer().serializeToString(xml));
    var $xml = $(xml);
    var $contactsContent = $('#contactsContent');

    $contactsContent.append($('<ul id="contactsList">\n\
                    </ul>'));
    var $contactsList = $("#contactsList");

    $xml.find('user').each(function () {
        if ($(this).find('userId').text() != loggedUser) {
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

    $groupsContent.append($('<ul id="groupsList">\n\
                    </ul>'));
    var $groupsList = $("#groupsList");

    $xml.find('group').each(function () {
        $groupsList.append('<li><button id="group-chat-button">' + $(this).find('name').text()) + '</button></li>';
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
    var firstname = '', lastname = '';

    // clear message container first
    $messagesContainer.empty();

    $xml.find('message').each(function () {

        var $messageData = $(this);
        var $uid = $messageData.find('fromUserId').text();

        $.ajax({
            url: baseURL + '/users/' + $uid,
            method: 'GET',
            dataType: 'xml',
            success: function (userXml) {
                firstname = $(userXml).find('firstname').text();
                lastname = $(userXml).find('lastname').text();
            },
            error: function () {
                // just in case nothing is found...
                firstname = 'unknown';
                lastname = 'user';
            },
            complete: function () {
                // message was sent by me, style it accordingly
                if ($uid == loggedUser) {
                    $messagesContainer.append('<div class="my-message">' +
                            '<p class="message-body">' +
                            $messageData.find('text').text() + '</p>' +
                            '<p class="timestamp">' +
                            toTime($messageData.find('timestamp').text()) + '</p>' +
                            '</div>'
                            );
                    // message was sent by someone else
                } else {
                    $messagesContainer.append('<div class="others-message">' +
                            '<p class="username">' +
                            firstname + ' ' +
                            lastname +
                            '</p>' +
                            '<p class="message-body">' +
                            $messageData.find('text').text() +
                            '</p>' +
                            '<p class="timestamp">' +
                            toTime($messageData.find('timestamp').text()) +
                            '</p>' +
                            '</div>'
                            );
                }
            }
        });


    });

    // finally, scroll message container to bottom
    $messagesContainer.animate({scrollTop: $messagesContainer[0].scrollHeight}, 500);

}

function sendMessage(message) {
    // populate fromUser with getuser by id
    //var message = messageContent;
    //var fromUser = getUserXml(loggedUser);
    //console.log("message being readied for send: " + message);
    //console.log("fromUser: " + fromUser);
    /*
     var messageXML =
     $.parseXML('');
     var messageXMLDoc = $.parseXML(messageXML);
     console.log(messageXMLDoc);
     var $xml = $(messageXMLDoc);
     */

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
                //dataType: 'xml',
                success: function () {
                    // update messages on successful send
                    getBroadcasts();
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

//Work in progress. Trying to get the group backlog and show it on chat window
function getGroupMessages(groupid){
    console.log('In getPrivate');
    $.ajax({
        url: baseURL + '/messages/'+groupid,
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
    }

function validateInput(input) {
    // returns true for valid text content (no empty string or just whitespace)
    var value = $.trim(input);

    if (value.length > 0) {
        return true;
    }

    return false;
}
