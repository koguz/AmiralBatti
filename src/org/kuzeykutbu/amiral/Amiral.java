/*
    Amiral Battı, Java ME
    Copyright (C) 2009  Kaya Oğuz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.kuzeykutbu.amiral;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * 
 * @author kaya
 */
public class Amiral extends MIDlet
{
    Uygulama oyun;
    protected void startApp() throws MIDletStateChangeException
    {
        if (oyun == null)
        {
            oyun = new Uygulama(this);
            oyun.baslat();
        }
    }

    protected void pauseApp()
    {
        
    }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException
    {
        
    }

    public void quitApp()
    {
        try { destroyApp(true); notifyDestroyed(); }
        catch(Exception e) {}
    }

}
