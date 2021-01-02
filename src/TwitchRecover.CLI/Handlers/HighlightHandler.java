/*
 * Copyright (c) 2021 Daylam Tayari <daylam@tayari.gg>
 *
 * This library is free software. You can redistribute it and/or modify it under the terms of the GNU General Public License version 3 as published by the Free Software Foundation.
 * This program is distributed in the that it will be use, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/*
 * @author Daylam Tayari https://github.com/daylamtayari
 * @version 2.0
 * Github project home page: https://github.com/TwitchRecover
 * Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

package TwitchRecover.CLI.Handlers;

import TwitchRecover.Core.Enums.FileExtension;
import TwitchRecover.Core.Feeds;
import TwitchRecover.Core.Highlights;

import java.util.Scanner;

/**
 * HighlightHandler object class which
 * handles the highlight prompts.
 */
public class HighlightHandler {
    private int option;     //Integer value representing the user's selected option.

    /**
     * Constructor and main handler
     * of the HighlightHandler object class.
     * @param option    Integer value that represents the user's selected option.
     */
    public HighlightHandler(int option){
        this.option=option;
        if(option==7){
            download();
        }
        else{
            recover();
        }
    }

    /**
     * This method processes the downloading of a
     * highlight.
     */
    private void download(){
        Scanner sc=new Scanner(System.in);
        System.out.print(
                  "\n\nHighlight downloading:"
                + "\nPlease enter the link of the highlight to download: "
        );
        String highlightURL=sc.nextLine();
        while(!CoreHandler.isVideo(highlightURL)){
            System.out.print(
                      "\n\nERROR: Invalid highlight link."
                    + "\nPlease enter a valid highlight URL."
            );
            highlightURL=sc.nextLine();
        }
        Highlights highlight=new Highlights(false);
        highlight.retrieveID(highlightURL);
        Feeds feeds=highlight.retrieveHighlightFeeds();
        int quality=CoreHandler.selectFeeds(feeds);
        FileExtension fe=CoreHandler.userFE();
        highlight.downloadHighlight(fe, feeds.getFeed(quality));
        sc.close();
        System.out.print("\nFile downloaded at: " + highlight.getFFP());
    }

    /**
     * This method processes and handles
     * the recovery of a highlight.
     */
    private void recover(){
        Scanner sc=new Scanner(System.in);
        Highlights highlight=new Highlights(true);
        System.out.print(
                  "\n\nHighlight recovery:"
                + "\nPlease enter the exact details about the highlight in order to retrieve it:"
                + "\nChannel name: "
        );
        highlight.setChannel(sc.nextLine());
        System.out.print("\nStream ID: ");
        highlight.setStreamID(sc.nextLine());
        System.out.print("\nHighlight ID: ");
        highlight.setID(Long.parseLong(sc.nextLine()));
        System.out.print("\nTimestamp (YYYY-MM-DD HH:mm:ss format): ");
        highlight.setTimestamp(sc.nextLine());
        highlight.retrieveHighlights();
        int quality=CoreHandler.selectFeeds(highlight.retrieveHighlightFeeds());
        System.out.print("\n\nM3U8 link: "+highlight.getFeed(quality));
    }
}