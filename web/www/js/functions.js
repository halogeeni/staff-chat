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
var baseURL = "http://localhost:8080/staff-chat/webresources";

// development login flag, so that we are "logged in" as a specific user
var loggedUser = 0;

// Fix so that the user doesn't show in the contacts
function listContacts(xml, status) {
    console.log('In listContacts');
    xmlString = (new XMLSerializer().serializeToString(xml));
    var $xml = $(xml);
    var $contactsContent = $('#contactsContent');

    $contactsContent.append($('<table id="contactsTable">\n\
                    </table>'));
    var $contactsTable = $("#contactsTable");

    $xml.find('user').each(function () {
        $contactsTable.append('<tr><td><button>' + $(this).find('firstname').text()
                + $(this).find('lastname').text() + '</button></td></tr>');
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
    xmlString = (new XMLSerializer().serializeToString(xml));
    var $xml = $(xml);
    var $groupsContent = $('#groupsContent');

    $groupsContent.append($('<table id="groupsTable">\n\
                    </table>'));
    var $groupsTable = $("#groupsTable");

    $xml.find('group').each(function () {
        $groupsTable.append('<tr><td><button>' + $(this).find('name').text()) + '</button></td></tr>';
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


function listMessages(xml, status) {
    console.log('In listMessages');
    xmlString = (new XMLSerializer().serializeToString(xml));
    var $xml = $(xml);
    var $messagesContainer = $('#messages');

    $xml.find('message').each(function () {
        var $uid = $(this).find('userId').text();

        if ($uid == loggedUser) {
            $messagesContainer.append('<div class="my-message">' +
                    '<p class="message-body">' +
                    $(this).find('text').text() + '</p>' +
                    '<p class="timestamp">' +
                    $(this).find('timestamp').text() + '</p>' +
                    '</div>'
                    );
        } else {
            $messagesContainer.append('<div class="others-message">' +
                    '<p class="username">' +
                    $(this).find('firstname').text() + ' ' +
                    $(this).find('lastname').text() +
                    '</p>' +
                    '<p class="message-body">' +
                    $(this).find('text').text() +
                    '</p>' +
                    '<p class="timestamp">' +
                    $(this).find('timestamp').text() +
                    '</p>' +
                    '</div>'
                    );
        }
    });
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
