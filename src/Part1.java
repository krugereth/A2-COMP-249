import java.io.*;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.*;

public class Part1 {
    protected static int CCBcountCopy;
    protected static int HCBcountCopy;
    protected static int MTVcountCopy;
    protected static int MRBcountCopy;
    protected static int NEBcountCopy;
    protected static int OTRcountCopy;
    protected static int SSMcountCopy;
    protected static int TPAcountCopy;

    public static void do_part1() {
        String title, authors, price, ISBN, genre, year;
        int CCBcount = 0, HCBcount = 0, MTVcount = 0, MRBcount = 0, NEBcount = 0, OTRcount = 0, SSMcount = 0, TPAcount = 0;
        int invalidGenreCount = 0, tooManyFieldsCount = 0, tooFewFieldsCount = 0, missingFieldCount = 0;


        System.out.println("Welcome to my book sorting program!");

        Scanner sc = null;
        PrintWriter pw = null;

        try {
            sc = new Scanner(new FileInputStream("oart1_input_file_names.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            System.exit(0);
        }

        // Define fileCount and numFiles to go through each file

        int numFiles = sc.nextInt();
        int fileCounter = 0;

        while (fileCounter < numFiles) {
            fileCounter++;
            String fileName = sc.nextLine();
            Scanner readData = null;

            try {
                readData = new Scanner(new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
                System.exit(0);
            }

            while (readData.hasNextLine()) {
                String recordedRecord = readData.nextLine();
                String originalRecord = recordedRecord;

                if (recordedRecord.charAt(0) == '"') {
                    title = "\"" + recordedRecord.substring(1, recordedRecord.indexOf("\"", 2)) + "\"";
                    recordedRecord = recordedRecord.substring(recordedRecord.indexOf('\"', 3));
                } else {
                    title = recordedRecord.substring(0, recordedRecord.indexOf(','));
                    recordedRecord = recordedRecord.substring(recordedRecord.indexOf(',', 2));
                }

                char ch = ',';
                int charCount = 0;
                String rec = originalRecord;

                if (originalRecord.charAt(0) == '\"') {
                    rec = originalRecord.substring(originalRecord.indexOf('\"', 3));
                }

                for (int i = 0; i < rec.length(); i++) {
                    if (rec.charAt(i) == ch)
                        charCount++;
                }


                price = recordedRecord.substring(recordedRecord.indexOf(',') + 1, recordedRecord.indexOf(',', 2));
                recordedRecord = recordedRecord.substring(recordedRecord.indexOf(',', 2));
                ISBN = recordedRecord.substring(recordedRecord.indexOf(',') + 1, recordedRecord.indexOf(',', 2));
                recordedRecord = recordedRecord.substring(recordedRecord.indexOf(',', 2));
                recordedRecord = recordedRecord.substring(recordedRecord.indexOf(',', 2));
                year = recordedRecord.substring(recordedRecord.indexOf(',') + 1);
                genre = recordedRecord.substring(recordedRecord.indexOf(',') + 1, recordedRecord.indexOf(',', 2));
                authors = recordedRecord.substring(recordedRecord.indexOf(",") + 1, recordedRecord.indexOf(',', 2));
                recordedRecord = recordedRecord.substring(recordedRecord.indexOf(',', 2));

                int numFields = charCount + 1;
                try {
                    if (numFields > 6) {
                        throw new TooManyFieldsException("Error: Too Many Fields");
                    } else if (numFields < 6) {
                        throw new TooFewFieldsException("Error: Too Few Fields");
                    } else if (genre.equals("CCB") || genre.equals("HCB") || genre.equals("MTV") || genre.equals("MRB") ||
                            genre.equals("NEB") || genre.equals("OTR") || genre.equals("SSM") || genre.equals("TPA")) {
                        throw new UnknownGenreException("Error: Unknown Genre");
                    } else if (title.equals(" ") || authors.equals(" ") || price.equals(" ") || ISBN.equals(" ") || genre.equals(" ") || year.equals(" ")) {
                        throw new MissingFieldException("Error: Missing Field");
                    }
                } catch (TooManyFieldsException e) {
                    e.getMessage();
                } catch (TooFewFieldsException e) {
                    e.getMessage();
                } catch (UnknownGenreException e) {
                    e.getMessage();
                } catch (MissingFieldException e) {
                    e.getMessage();
                }

                if (title.equals(" ") || authors.equals(" ") || price.equals(" ") || ISBN.equals(" ") || genre.equals(" ") || year.equals(" ") || year.isEmpty()) {
                    missingFieldCount++;
                    pw.println("\nsyntax error in file: " + fileName
                            + "\n===================="
                            + "\nError: missing field"
                            + "\nRecord: " + originalRecord);
                    pw.flush();
                } else if (numFields > 6) {
                    tooManyFieldsCount++;
                    pw.println("\nsyntax error in file: " + fileName
                            + "\n===================="
                            + "\nError: too many fields"
                            + "\nRecord: " + originalRecord);
                    pw.flush();
                } else if (numFields < 6) {
                    tooFewFieldsCount++;
                    pw.println("\nsyntax error in file: " + fileName
                            + "\n===================="
                            + "\nError: too few fields"
                            + "\nRecord: " + originalRecord);
                    pw.flush();
                } else {
                    //print writer object that will write in each genre's file
                    PrintWriter genreFileWriter = null;

                    switch (genre) {
                        case "CCB":
                            CCBcount++;
                            try {
                                genreFileWriter = new PrintWriter(new FileOutputStream("Cartoons_Comics_Books.csv.txt", true));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            genreFileWriter.println(title + "," + authors + "," + price + "," + ISBN + "," + genre + "," + year);
                            genreFileWriter.flush();
                            genreFileWriter.close();
                            break;

                        case "HCB":
                            HCBcount++;
                            try {
                                genreFileWriter = new PrintWriter(new FileOutputStream("Hobbies_Collectibles_Books.csv.txt", true));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            genreFileWriter.println(title + "," + authors + "," + price + "," + ISBN + "," + genre + "," + year);
                            genreFileWriter.flush();
                            genreFileWriter.close();
                            break;

                        case "MTV":
                            MTVcount++;
                            try {
                                genreFileWriter = new PrintWriter(new FileOutputStream("Movies_TV.csv.txt", true));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            genreFileWriter.println(title + "," + authors + "," + price + "," + ISBN + "," + genre + "," + year);
                            genreFileWriter.flush();
                            genreFileWriter.close();
                            break;

                        case "MRB":
                            MRBcount++;
                            try {
                                genreFileWriter = new PrintWriter(new FileOutputStream("Music_Radio_Books.csv.txt", true));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            genreFileWriter.println(title + "," + authors + "," + price + "," + ISBN + "," + genre + "," + year);
                            genreFileWriter.flush();
                            genreFileWriter.close();
                            break;

                        case "NEB":
                            NEBcount++;
                            try {
                                genreFileWriter = new PrintWriter(new FileOutputStream("Nostalgia_Eclectic_Books.csv.txt", true));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            genreFileWriter.println(title + "," + authors + "," + price + "," + ISBN + "," + genre + "," + year);
                            genreFileWriter.flush();
                            genreFileWriter.close();
                            break;

                        case "OTR":
                            OTRcount++;
                            try {
                                genreFileWriter = new PrintWriter(new FileOutputStream("Old_Time_Radio.csv.txt", true));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            genreFileWriter.println(title + "," + authors + "," + price + "," + ISBN + "," + genre + "," + year);
                            genreFileWriter.flush();
                            genreFileWriter.close();
                            break;

                        case "SSM":
                            SSMcount++;
                            try {
                                genreFileWriter = new PrintWriter(new FileOutputStream("Sports_Sports_Memorabilia.csv.txt", true));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            genreFileWriter.println(title + "," + authors + "," + price + "," + ISBN + "," + genre + "," + year);
                            genreFileWriter.flush();
                            genreFileWriter.close();
                            break;

                        case "TPA":
                            TPAcount++;
                            try {
                                genreFileWriter = new PrintWriter(new FileOutputStream("Trains_Planes_Automobiles.csv.txt", true));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            genreFileWriter.println(title + "," + authors + "," + price + "," + ISBN + "," + genre + "," + year);
                            genreFileWriter.flush();
                            genreFileWriter.close();
                            break;

                        default:
                            invalidGenreCount++;
                            pw.println("\nsyntax error in file: " + fileName
                                    + "\n===================="
                                    + "\nError: invalid genre"
                                    + "\nRecord: " + originalRecord);
                            pw.flush();
                    }
                }
            }
            readData.close();
        }

        pw.println("\nI"+ (tooManyFieldsCount+tooFewFieldsCount+missingFieldCount+invalidGenreCount) + " records with syntax errors,"
                + " and " + (CCBcount + HCBcount + MTVcount + MRBcount + NEBcount + OTRcount + SSMcount + TPAcount) + " syntactically valid records.");
        pw.flush();
        pw.close();
        sc.close();

        CCBcountCopy = CCBcount;
        HCBcountCopy = HCBcount;
        MTVcountCopy = MTVcount;
        MRBcountCopy = MRBcount;
        NEBcountCopy = NEBcount;
        OTRcountCopy = OTRcount;
        SSMcountCopy = SSMcount;
        TPAcountCopy = TPAcount;
    }
}

