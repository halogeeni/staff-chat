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
package Chat;

import java.awt.Image;
import javax.xml.bind.annotation.XmlElement;

public class MessageBody {

    // text and images only so far
    private final String text;
    private final Image image;

    public MessageBody(String text) {
        this.text = text;
        this.image = null;
    }

    public MessageBody(Image image) {
        this.text = null;
        this.image = image;
    }

    // getters
    public boolean hasText() {
        return text != null;
    }

    public boolean hasImage() {
        return image != null;
    }

    @XmlElement
    public String getText() {
        return text;
    }

    @XmlElement
    public Image getImage() {
        return image;
    }

}
