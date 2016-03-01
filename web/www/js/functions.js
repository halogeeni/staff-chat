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
// chat selection flags
var selectedGroup = 0, selectedUser = 0, timerId = 0;
// simple login flag for demo purposes
var loggedIn = false;

// parse GET variables from URL
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] === variable) {
            return pair[1];
        }
    }
    return(false);
}

// login demo functionality
function login() {
    var username = getQueryVariable("username");
    var password = getQueryVariable("password");

    if ((username === "user0") && (password === "pass")) {
        return 0;
    } else if ((username === "user1") && (password === "pass")) {
        return 1;
    } else if ((username === "user2") && (password === "pass")) {
        return 2;
    } else if ((username === "user3") && (password === "pass")) {
        return 3;
    } else {
        return 0;
    }
}

// Turns milliseconds time to a timestamp
function toTime(s) {
    var myDate = new Date(s * 1);
    return myDate.toLocaleString();
}

// Lists contacts
function listContacts(xml) {
    var $xml = $(xml);
    var $contactContainer = $('#contactsContainer');
    $contactContainer.append($('<form action="javascript:void(0);"><ul id="contactsList">\n\</ul></form>'));
    var $contactsList = $("#contactsList");

    $xml.find('user').each(function () {
        if (parseInt($(this).find('userId').text()) !== loggedUser) {
            $contactsList.append(
                    '<li><button value="' +
                    $(this).find('userId').text() +
                    '"' +
                    'class="private-chat-button">' +
                    $(this).find('firstname').text() +
                    " " +
                    $(this).find('lastname').text() +
                    '</button></li>'
                    );
        }
    });

    // Opens private chat
    $(".private-chat-button").click(function (event) {
        clearInterval(timerId);
        event.preventDefault();
        selectedUser = parseInt($(this).attr("value"));
        $('#contactsContent').empty();
        
        $xml.find('user').each(function () {
            if (parseInt($(this).find('userId').text()) === selectedUser) {
                $('#currentGroup').append(
                        $(this).find('firstname').text() 
                        + " " + 
                        $(this).find('lastname').text()
                    );
                }
            });
        $("#container").load("privateChat.html").fadeIn('500');
    });
}

// Lists groups
function listGroups(xml) {
    var $xml = $(xml);
    var $contactContainer = $('#contactsContainer');

    $contactContainer.append($('<form action="javascript:void(0);"><ul id="contactsList"></ul></form>'));
    var $contactsList = $("#contactsList");

    $xml.find('group').each(function () {
        $contactsList.append('<li><button value="' +
                $(this).find('id').text() +
                '" ' +
                'class="group-chat-button">' +
                $(this).find('name').text() +
                '</button></li>'
                );
    });

    // group buttons' click event handler
    $(".group-chat-button").click(function (event) {
        clearInterval(timerId);
        event.preventDefault();
        selectedGroup = parseInt($(this).attr("value"));
        $('#contactContainer').empty();
        
        $xml.find('group').each(function () {
            if (parseInt($(this).find('id').text()) === selectedGroup) {
                $('#currentGroup').append($(this).find('name').text());
            }
        });
        
        $("#container").load("groupChat.html").fadeIn('500');
    });

}

function getContacts() {
    $.ajax({
        url: baseURL + '/users',
        method: 'GET',
        dataType: 'xml',
        success: listContacts
    });
}

function getGroups() {
    $.ajax({
        url: baseURL + '/groups',
        method: 'GET',
        dataType: 'xml',
        success: listGroups
    });
}

function getBroadcasts() {
    $.ajax({
        url: baseURL + '/messages/broadcast',
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
}

function getGroupMessages(groupid) {
    $.ajax({
        url: baseURL + '/messages/group/' + groupid,
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
}

function getPrivateMessages(userid) {
    $.ajax({
        url: baseURL + '/messages/' + loggedUser + '/private/' + userid,
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
}

// Message listing
function listMessages(xml) {
    var $xml = $(xml);
    var $messagesContainer = $('#messages');
    var messageBuffer = [];
    var promises = [];

    // Get the current number of messages
    var currentMessageCount = $xml.find('message').size();

    // Update message view only when new messages are available
    if (currentMessageCount > messageCount) {
        $xml.find('message').each(function () {
            var $messageData = $(this);
            var uid = parseInt($messageData.find('fromUserId').text());
            var firstname = '', lastname = '';

            // Push AJAX call promise to array
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

                            // Message was sent by me, style it accordingly
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
                                // Message was sent by someone else
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

                            // Convert timestamp string to integer
                            timestamp = parseInt(timestamp);
                            messageBuffer.push({timestamp: timestamp, message: messageHTML});
                        }
                    }));
        });

        // MESSAGE BUFFERING DONE
        // Wait for AJAX calls to complete
        $.when.apply($, promises).done(function () {
            // Sort the buffer by timestamps
            messageBuffer.sort(function (x, y) {
                return x.timestamp - y.timestamp;
            });

            // Clear the message container
            $messagesContainer.empty();

            // Output newly-populated message buffer to the container
            for (var i = 0; i < messageBuffer.length; i++) {
                $messagesContainer.append($.parseHTML((messageBuffer[i].message)));
            }

            // check if user has scrolled to somewhat near bottom ...
            if ($messagesContainer.scrollTop() + 200 >=
                    ($messagesContainer.prop('scrollHeight') -
                            $messagesContainer.prop('offsetHeight'))) {
                // ... if so, then scroll message container to bottom
                $messagesContainer.animate({scrollTop: $messagesContainer.prop('scrollHeight')}, 500);
            }

        });

    }

    // Update message counter
    messageCount = currentMessageCount;

}

function sendMessage(message, channel) {
    var xml = '';

    // -1 value for timestamp is a workaround
    if (channel === 'CHANNEL_BROADCAST') {
        xml = "<message><body><text></text></body><channel></channel><fromUserId></fromUserId><messageId>-1</messageId></message>";
    } else if (channel === 'CHANNEL_GROUP') {
        xml = "<message><body><text></text></body><channel></channel><fromUserId></fromUserId><toGroupId></toGroupId><messageId>-1</messageId></message>";
    } else if (channel === 'CHANNEL_PRIVATE') {
        xml = "<message><body><text></text></body><channel></channel><fromUserId></fromUserId><toUserId></toUserId><messageId>-1</messageId></message>";
    }

    var xmlDoc = $.parseXML(xml);
    var $xml = $(xmlDoc);
    var serializer = new XMLSerializer();

    $.ajax({
        url: baseURL + '/users/' + loggedUser,
        method: 'GET',
        dataType: 'xml',
        success: function (xml) {
            // Append sender's user id to the XML
            $xml.find('fromUserId').append(loggedUser);
            // Append message body (text)
            $xml.find('text').append(message);
            // Append channel info
            $xml.find('channel').append(channel);
            // Append toGroupId in case of a group message
            if (channel === 'CHANNEL_GROUP') {
                $xml.find('toGroupId').append(selectedGroup);
            } else if (channel === 'CHANNEL_PRIVATE') {
                $xml.find('toUserId').append(selectedUser);
            }

            // Serialize the XML for sending
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
                    // Empty message-field
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
    $.ajax({
        url: baseURL + '/messages/broadcast',
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
}

function getGroupMessages(groupid) {
    $.ajax({
        url: baseURL + '/messages/group/' + groupid,
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
}

function getPrivateMessages(userid) {
    $.ajax({
        url: baseURL + '/messages/' + loggedUser + '/private/' + userid,
        method: 'GET',
        dataType: 'xml',
        success: listMessages
    });
}

function escapeHtml(text) {
    var map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };

    return text.replace(/[&<>"']/g, function (m) {
        return map[m];
    });
}

function validateInput(input) {
    // just a simple input check for an empty string or plain whitespace
    if (input.trim() == null || input.trim() == "" || input === " ") {
        return false;
    } else {
        return true;
    }
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
                    firstname +
                    ' ' +
                    lastname +
                    '<br>' +
                    '<i>job title</i>'
                    );
            $('#loggedInAs').append(userHTML);
        },
        complete: function () {
            loggedIn = true;
        }
    });
}
