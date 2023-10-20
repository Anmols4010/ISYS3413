import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Artist {
    private String ID;
    private String Name;
    private String Address;
    private String Birthdate;
    private String Bio;
    private ArrayList<String> Occupations;
    private ArrayList<String> Genres;
    private ArrayList<String> Awards;

    public Artist(String id, String name, String address, String birthdate, String bio,
                  ArrayList <String> occupations, ArrayList <String> genres, ArrayList <String> awards) {
        ID = id;
        Name = name;
        Address = address;
        Birthdate = birthdate;
        Bio = bio;
        Occupations = occupations;
        Genres = genres;
        Awards = awards;
    }

    public boolean addArtist() {
        // Condition 1: Check Artist ID
        if (!ID.matches("[5-9]{3}[A-Z]{5}[!@#$%^&*]{2}")) {
            return false;
        }
        // Condition 2: Check Birth Date format
        if (!Birthdate.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            return false;
        }
        // Condition 3: Check Address format
        if (!Address.matches("^[A-Za-z]+\\|[A-Za-z]+\\|[A-Za-z]+$")) {
            return false;
        }
        // Condition 4: Check Bio length
        String[] words = Bio.split(" ");
        int wordCount = words.length;
        if (wordCount < 10 || wordCount > 30) {
            return false;
        }
        // Condition 5: Check Occupations
        if (Occupations.isEmpty() || Occupations.size() > 5) {
            return false;
        }
        // Condition 6: Check Awards
        if (Awards.size() > 3) {
            return false;
        }
        for (String award : Awards) {
            String[] parts = award.split(",");
            if (parts.length != 2) {
                return false;
            }
            String title = parts[1].trim();
            if (title.length() < 4 || title.length() > 10) {
                return false;
            }
        }
        // Condition 7: Check Genres
        if (Genres.size() < 2 || Genres.size() > 5) {
            return false;
        }
        if (Genres.contains("pop") && Genres.contains("rock")) {
            return false;
        }
        // If all conditions are met, add the artist's information to the TXT file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("artists.txt", true));
            writer.write("ID: "+ ID +"\n"+ "Name: " + Name + "\n" +"Address: " + Address +"\n" + "DOB: " + Birthdate + "\n"+"Bio: " + Bio + "\n"+"Occupations: "
                    + String.join(",", Occupations) +"\n"+ "Genres: " + String.join(",", Genres) + "\n"+"Awards: "
                    + String.join(",", Awards) + "\n");
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateArtist() {
        // Condition 1: Check all constraints discussed for the addArtist function
        if (!ID.matches("[5-9]{3}[A-Z]{5}[%_]{2}")) {
            return false;
        }
        // Condition 2: Check Birth Date format
        if (!Birthdate.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            return false;
        }
        // Condition 3: Check Address format
        if (!Address.matches("^[A-Za-z]+\\|[A-Za-z]+\\|[A-Za-z]+$")) {
            return false;
        }
        // Condition 4: Check Bio length
        String[] words = Bio.split(" ");
        int wordCount = words.length;
        if (wordCount < 10 || wordCount > 30) {
            return false;
        }
        // Condition 5: Check Occupations
        if (Occupations.isEmpty() || Occupations.size() > 5) {
            return false;
        }
        // Condition 6: Check Awards
        if (Awards.size() > 3) {
            return false;
        }
        for (String award : Awards) {
            String[] parts = award.split(",");
            if (parts.length != 2) {
                return false;
            }
            String title = parts[1].trim();
            if (title.length() < 4 || title.length() > 10) {
                return false;
            }
        }
        // Condition 7: Check Genres
        if (Genres.size() < 2 || Genres.size() > 5) {
            return false;
        }
        if (Genres.contains("pop") && Genres.contains("rock")) {
            return false;
        }

        // Condition 2: If an artist was born before 2000, their occupation cannot be changed
        int birthYear = Integer.parseInt(Birthdate.split("-")[2]);
        if (birthYear < 2000) {
            if (!Occupations.equals(getArtistOccupationsFromTXTFile(ID))) {
                return false;
            }
        }

        // Condition 3: Awards given before 2000 cannot be changed
        if (!canUpdateAwards(Awards, getArtistAwardsFromTXTFile(ID))) {
            return false;
        }

        // If all conditions are met, update the artist's information in the TXT file
        try {
            ArrayList<String> lines = new ArrayList<>();
            File inputFile = new File("artists.txt");
            File tempFile = new File("artists_temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile, true));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String artistID = currentLine.split("\\|")[0];
                if (artistID.equals(ID)) {
                    // Update the artist's information
                    writer.write("ID: "+ ID +"\n"+ "Name: " + Name + "\n" +"Address: " + Address +"\n" + "DOB: " + Birthdate + "\n"+"Bio: " + Bio + "\n"+"Occupations: "
                            + String.join(",", Occupations) +"\n"+ "Genres: " + String.join(",", Genres) + "\n"+"Awards: "
                            + String.join(",", Awards) + "\n");
                } else {
                    writer.write(currentLine + "\n");
                }
            }

            reader.close();
            writer.close();
            if (!inputFile.delete()) {
                return false;
            }
            if (!tempFile.renameTo(inputFile)) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper function to retrieve the artist's current occupations from the TXT file
    private ArrayList<String> getArtistOccupationsFromTXTFile(String artistID) {
        ArrayList<String> occupations = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("artists.txt"));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split("\\|");
                if (parts[0].equals(artistID)) {
                    String[] occupationArray = parts[5].split(",");
                    for (String occupation : occupationArray) {
                        occupations.add(occupation);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return occupations;
    }

    // Helper function to retrieve the artist's current awards from the TXT file
    private ArrayList<String> getArtistAwardsFromTXTFile(String artistID) {
        ArrayList<String> awards = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("artists.txt"));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split("\\|");
                if (parts[0].equals(artistID)) {
                    String[] awardArray = parts[7].split(",");
                    for (String award : awardArray) {
                        awards.add(award);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return awards;
    }

    // Helper function to check if awards can be updated based on the year
    private boolean canUpdateAwards(ArrayList<String> newAwards, ArrayList<String> currentAwards) {
        for (String newAward : newAwards) {
            String[] newParts = newAward.split(",");
            int newYear = Integer.parseInt(newParts[0]);
            if (newYear < 2000) {
                return false; // Awards given before 2000 cannot be changed
            }
            for (String currentAward : currentAwards) {
                String[] currentParts = currentAward.split(",");
                int currentYear = Integer.parseInt(currentParts[0]);
                if (currentYear == newYear) {
                    return false; // Year of awards cannot be changed
                }
            }
        }
        return true;
    }
    public static void main(String[] args) {
        Artist A= new Artist("768QWERT*%","Test","Melbourne|Victoria|Australia","18-08-2005","Hello, this test should pass with flying colors nine ten",new ArrayList<>(Arrays.asList("Singer", "Guitarist", "Actor", "Painter", "Writer")),new ArrayList<>(Arrays.asList("Pop", "classical", "jazz", "Hip hop", "Country music")),new ArrayList<>(Arrays.asList("2010,Award1", "2011,Award2", "2012,Award3")));
        A.addArtist();
    }
}



