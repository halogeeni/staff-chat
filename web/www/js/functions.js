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
    xmlString = (new XMLSerializer().serializeToString(xml));
    var $xml = $(xml);
    var $contactsContent = $('#contactsContent');

    $contactsContent.append($('<ul id="contactsList">\n\
                    </ul>'));
    var $contactsList = $("#contactsList");

    $xml.find('user').each(function () {
        $contactsList.append('<li><button>' + $(this).find('firstname').text()+
                " "+ $(this).find('lastname').text() + '</button></li>');
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

    $groupsContent.append($('<ul id="groupsList">\n\
                    </ul>'));
    var $groupsList = $("#groupsList");

    $xml.find('group').each(function () {
        $groupsList.append('<li><button>' + $(this).find('name').text()) + '</button></li>';
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
                    toTime($(this).find('timestamp').text()) + '</p>' +
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
                    toTime($(this).find('timestamp').text()) +
                    '</p>' +
                    '</div>'
                    );
        }
    });
}


function sendMessage(message){
    //populate fromUser with getuser by id
    var message=message;
    var fromUser=getUser(loggedUser); //doesn't have a return yet
    console.log("message being readied for send: "+message);
    console.log("fromUser: "+fromUser);
    var messageXMLDoc=$.parseXML('<message><body><text></text></body><channel></channel><fromUser></fromUser></message>');
    var $messageXML=$(messageXMLDoc);
    
    $messageXML.find('text').append(message);
    
    
    xmlString=(new XMLSerializer()).serializeToString(messageXMLDoc);
    console.log('messageXmlDoc serialized: '+xmlString);
    
    $.ajax({
        url: baseURL + "/messages/add",
        data: messageXMLDoc,
        processData:false,
        type:'POST',
        contentType: 'application/xml',
        dataType: 'xml'
    });

}




function getUser(userid){
    console.log("getUser user value to be fetched: "+userid);
    var userid=userid;
    var xmlUser=null;
    $.ajax({
        url: baseURL + '/users/'+userid,
        method: 'GET',
        dataType: 'xml',
        success: function (data){
            xmlUser=(new XMLSerializer()).serializeToString(data);
            console.log("getUser() -> userXML to string:"+ xmlUser);
            return xmlUser;
        }
    });
    //needs to return something
    return xmlUser;
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
