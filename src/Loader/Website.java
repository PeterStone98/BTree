/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.jsoup.nodes.Document;

/**
 *
 * @author Pete
 */
public class Website implements Serializable {

    //Document document;
    private String text;
    public String title;
    private String pre1 = "";
    private String pre2 = "";
    public BTree tree;//BTree
    public Boolean mediod = false;//boolean to mark if website is a mediod center
    public ArrayList<Integer> frequencyList = new ArrayList<>();//frequency list used for comparison
    public ArrayList<String> topWords = new ArrayList<>();//list of top words
    public ArrayList<Integer> topWordsFreq = new ArrayList<>();//list of top word frequencies
    public ArrayList<Website> neighbors = new ArrayList<>();//list of neighbor websites
    public ArrayList<String> neighborsNames = new ArrayList<>();//list of neighbor website names
    boolean visited = false;//for traversal
    private String[] wordsToIgnore = {
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth",
        "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", //        "article", "using", "la", "up", "out", "make", "all", "any", "what", "when", "those", "who", "see", "if", "however",
        "and", "to", "the", "then", "there", "he", "she", "his", "her", "their", "they", "from", "that", "which", "was", "for", "are", "archived",
        "some", "have", "were", "this", "who", "on", "can", "but", "other", "more", "any", "all", "new", "what", "about", "pdf",
        "because", "use", "such", "many", "most", "also", "well", "isbn", "with", "only", "common", "thi", "into", "these",
        "time", "used", "see", "own", "while", "among", "however", "retrieved", "been", "had", "due", "main", "number", "during", "than",
        "when", "much", "became", "still", "found", "high", "atfer", "end", "line", "year", "making", "reference", "move", "total",
        "especially", "place", "take", "even", "seem", "might", "give", "few", "below", "list", "back", "without", "wherea", "until",
        "known", "another", "item", "through", "though", "mean", "need", "cannot", "those", "could", "now", "make", "after", "where",
        "include", " ", "together", "following", "using", "find", "different", "same", "given", "like", "change", "list", "example", "not",
        "tend", "follow", "led", "down", "choice", "les", "left", "right", "term", "above", "model", "continuou", "decrease", "sometime",
        "proces", "size", "set", "important", "significant", "running", "difference", "specific", "particular", "get", "aspect",
        "thing", "require", "increase", "decrease", "name", "behind", "combined", "there", "sent", "least", "said", "live", "note",
        "none", "told", "open", "field", "beyond", "out", "form", "generally", "show", "seen", "recent", "tried", "several", "almost",
        "very", "lead", "richard", "gregory", "including", "next", "day", "did", "just", "why", "under", "age", "michael", "how", "made", "act",
        "encyclopedia", "center", "original", "although", "impsct", "smaller", "seriou", "introduction", "race", "samuel", "primary",
        "paul", "barack", "wayne", "played", "steven", "etc", "present", "additional", "doe", "short", "large", "too", "gone", "according",
        "available", "established", "close", "required", "moment", "call", "classes", "instead", "meaning", "state", "middle", "whose",
        "usually", "real", "either", "type", "improvement", "often", "them", "case", "goal", "emil", "test", "idea", "card", "every", "would",
        "alan", "work", "described", "way", "finding", "rule", "post", "general", "pres", "called", "search", "definition", "divide", "method",
        "feature", "article", "history", "jack", "link", "john", "robert", "page", "design", "statement", "identifier", "university",
        "patent", "society", "home", "mark", "simple", "data", "effect", "life", "world", "museum", "education",
        "external", "court", "source", "information", "small", "censu", "north", "region", "built", "early", "david", "content",
        "part", "black", "implementation", "support", "version", "gap", "great", "throughout", "central", "ship", "major", "area", "view",
        "order", "studies", "location", "largest", "operate", "port", "unit", "instruction", "women", "being",
        "count", "charles", "control", "based", "container", "language", "modern", "able", "billion", "industry", "allow",
        "permanent", "string", "between", "south", "book", "level", "people", "matter", "acros", "last", "around", "held", "date", "regular",
        "pick", "published", "since", "done", "frequently", "becoming", "ago", "within", "know", "will", "create", "million", "study", "over",
        "table", "both", "exactly", "run", "except", "avoid", "placed", "enough", "eventually", "free", "instance", "event", "later", "further",
        "relatted", "result", "finally", "come", "creating", "widely", "concern", "long", "top", "before", "say", "play", "playing", "international",
        "culture", "standard", "sun", "intended", "contain", "comment", "multiple", "public", "near", "along", "here",
        "section", "william", "hundred", "needed", "via", "unlike", "late", "henry", "themselves", "moved", "saw", "begin", "giving",
        "began", "ahead", "discuss", "content", "again", "put", "ever", "article", "fast", "improving", "quality", "variety", "association"
    };

    //create obj and get text
    public Website() {
    }

    public Website(Document d) throws IOException, ClassNotFoundException {
        tree = new BTree();
        title = d.title();
        text = d.body().text();
        Scanner scan = new Scanner(text);

        while (scan.hasNext()) {
            //trim non letters and make lowercase
            String first = scan.next().replaceAll("\\p{Punct}", "").toLowerCase();

            //break after references(non essential words in the references)
            if (first.equals("referencesedit") || first.equals("notesedit")) {
                break;
            }
            //add word
            if (this.test(first)) {
                //get rid of plural instances of a word
                if (first.regionMatches(first.length() - 1, "s", 0, 1)) {
                    first = first.substring(0, first.length() - 1);
                }
                tree.add(first);
            }
            //create first phrase
            StringBuilder builder = new StringBuilder();
            if (scan.hasNext()) {
                String second = scan.next().replaceAll("\\p{Punct}", "").toLowerCase();
                if (second.equals("referencesedit") || second.equals("notesedit")) {
                    break;
                }

                if (this.test(second)) {
                    if (second.regionMatches(second.length() - 1, "s", 0, 1)) {
                        second = second.substring(0, second.length() - 1);
                    }
                    tree.add(second);
                }
                if (this.test(first) && this.test(second)) {
                    builder.append(first);
                    builder.append(" ");
                    builder.append(second);
                    String phrase1 = builder.toString();
                    if (!phrase1.contains("wiki") && !phrase1.contains("retrieved")) {
                        tree.add(phrase1);
                    }
                }
                //clear builder and make second phrase
                builder.delete(0, builder.length());
                if (scan.hasNext()) {
                    String third = scan.next().replaceAll("\\p{Punct}", "").toLowerCase();
                    if (third.equals("referencesedit") || third.equals("notesedit")) {
                        break;
                    }

                    if (this.test(third)) {
                        if (third.regionMatches(third.length() - 1, "s", 0, 1)) {
                            third = third.substring(0, third.length() - 1);
                        }
                        tree.add(third);
                    }
                    if (this.test(second) && this.test(third)) {
                        builder.append(second);
                        builder.append(" ");
                        builder.append(third);
                        String phrase2 = builder.toString();
                        if (!phrase2.contains("wiki") && !phrase2.contains("retrieved")) {
                            tree.add(phrase2);
                        }
                    }
                    //clear builder and make third phrase
                    builder.delete(0, builder.length());
                    if ((this.test(first) && this.test(third))) {
                        builder.append(first);
                        builder.append(" ");
                        builder.append(second);
                        builder.append(" ");
                        builder.append(third);
                        String phrase3 = builder.toString();
                        if (!phrase3.contains("wiki") && !phrase3.contains("retrieved")) {
                            tree.add(phrase3);
                        }
                    }
                    //clear builder and make fourth phrase
                    builder.delete(0, builder.length());
                    if (this.test(pre2) && this.test(first)) {
                        builder.append(pre2);
                        builder.append(" ");
                        builder.append(first);
                        String phrase4 = builder.toString();
                        if (!phrase4.contains("wiki") && !phrase4.contains("retrieved")) {
                            tree.add(phrase4);
                        }
                    }
                    //clear builder and make fifth phrase
                    builder.delete(0, builder.length());
                    if ((this.test(pre1) && this.test(first))) {
                        builder.append(pre1);
                        builder.append(" ");
                        builder.append(pre2);
                        builder.append(" ");
                        builder.append(first);
                        String phrase5 = builder.toString();
                        if (!phrase5.contains("wiki") && !phrase5.contains("retrieved")) {
                            tree.add(phrase5);
                        }
                    }
                    //clear builder and make sixth phrase
                    builder.delete(0, builder.length());
                    if ((this.test(pre2) && this.test(second))) {
                        builder.append(pre2);
                        builder.append(" ");
                        builder.append(first);
                        builder.append(" ");
                        builder.append(second);
                        String phrase6 = builder.toString();
                        if (!phrase6.contains("wiki") && !phrase6.contains("retrieved")) {
                            tree.add(phrase6);
                        }
                    }
                    this.pre1 = second;
                    this.pre2 = third;
                }
            }
        }
        scan.close();
        //get the top words from the btree
        for (int i = 0; i < tree.topWordsSize; i++) {
            if (tree.topWords[i].name.equals("")) {
                continue;
            }
            this.topWords.add(tree.topWords[i].name);
            this.topWordsFreq.add(tree.topWords[i].frequency);
        }
        this.text = null;//space saving reason
        this.wordsToIgnore = null;//space saving reason
        this.tree = null;//null btree for space saving reasons
    }

    Boolean test(String test) {
        if (Arrays.asList(wordsToIgnore).contains(test)) {
            return false;
            //doesn't add     
        } else if (!test.matches("\\p{L}+")) {
            return false;
            //dont inlcude numbers and non latin letters
        } else if (test.contains("wiki")) {
            return false;
        } else if (test.length() <= 2) {
            return false;
            //doesn't add insignificant words
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return this.title;
    }

}
