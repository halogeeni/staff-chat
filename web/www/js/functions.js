/* 
 * The MIT License
 *
 * Copyright 2016 aleksirasio.
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

// menu button handlers



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
    console.log('In getContact');
    $.ajax({
        url: baseURL + '/users',
        method: 'GET',
        dataType: 'xml',
        success: listContacts
    });
}
