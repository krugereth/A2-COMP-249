import java.io.*;
import java.util.Scanner;

public class Part2 extends Part1 {

    public static boolean isValidISBN(String isbn) {
        if (isbn.length() != 10 && isbn.length() != 13)
            return false;

        int sum = 0;
        if (isbn.length() == 10) {

            for (int i = 0; i < 10; i++) {
                char c = isbn.charAt(i);

                if (!Character.isDigit(c))
                    return false; // ISBN must consist only of digits, thus return false is letter appears

                int digit = Character.getNumericValue(c);
                sum += (10 - i) * digit;
            }
            return (sum % 11 == 0);

        } else {

            for (int i = 0; i < 13; i++) {
                char c = isbn.charAt(i);

                if (!Character.isDigit(c))
                    return false;

                int digit = Character.getNumericValue(c);
                sum += ((i % 2 == 0) ? digit : 3 * digit);
            }
            return (sum % 10 == 0);
        }
    }

    public static boolean isValidPrice(double price) {
        return price >= 0;
    }

    public static boolean isValidYear(int year) {
        return year >= 1995 && year <= 2010;
    }

    public static void serialize(Book[] array, String fileName) throws IOException {
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        //first line states how many records this file contains
        oos.writeInt(array.length);

        //loop through each element of the array and write it to the ObjectOutputStream
        for (Book book : array) {
            //if the current element is null, break out of the loop
            oos.writeObject(book);
        }

        oos.close();
        fos.close();
    }

    public static Book[] removeEmptyCells(Book[] array) {
        int count = 0;
        for (Book book : array) {
            if (!(book == null))
                count++;
        }

        Book[] newArray = new Book[count];
        int j = 0;
        for (Book book : array) {
            if (book == null) {
            }
            else {
                newArray[j] = book;
                j++;
            }
        }
        return newArray;
    }


    public static void do_part2() {

        Scanner sc = null;
        PrintWriter pw = null;

        try {
            String fileName = "part2_input_file_names.txt";
            sc = new Scanner(new FileInputStream(fileName));
            pw = new PrintWriter(new FileOutputStream("semantic_error_file.txt", true));

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found: ");
            System.exit(0); //terminate
        }

        int fileCount = 0; //number of files that will be scanned
        int numFiles = sc.nextInt(); //read the integer at the first line of the file
        sc.nextLine(); //go to next line

        String title, authors, ISBN, genre;
        double price;
        int year;

        //count the number of semantic errors and the number of valid book records
        int isbn10ErrorCount = 0, isbn13ErrorCount = 0, priceErrorCount = 0, yearErrorCount = 0;
        int ccb = 0, hcb = 0, mtv = 0, mrb = 0, neb = 0, otr = 0, ssm = 0, tpa = 0;

        //create Book arrays for each genre where length = number of syntactically valid records
        Book[] CCBbookArray = new Book[CCBcountCopy];
        Book[] HCBbookArray = new Book[HCBcountCopy];
        Book[] MTVbookArray = new Book[MTVcountCopy];
        Book[] MRBbookArray = new Book[MRBcountCopy];
        Book[] NEBbookArray = new Book[NEBcountCopy];
        Book[] OTRbookArray = new Book[OTRcountCopy];
        Book[] SSMbookArray = new Book[SSMcountCopy];
        Book[] TPAbookArray = new Book[TPAcountCopy];

        //while loop to go through each line of the input file
        while (fileCount < numFiles) {

            fileCount++; //increment at each iteration

            String fileName = sc.nextLine();
            Scanner recordScanner = null;

            try {
                recordScanner = new Scanner(new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found." + fileName);
            }


            if (recordScanner != null) {
                //while loop to keep going until the file has no other line
                while (recordScanner.hasNextLine()) {

                    String record = recordScanner.nextLine();
                    String originalRecord = record;

                    if (record.charAt(0) == '\"') {
                        title = record.substring(0, record.indexOf('\"', 3)) + "\"";
                        record = record.substring(record.indexOf('\"', 3));
                    } else {
                        title = record.substring(0, record.indexOf(','));
                        record = record.substring(record.indexOf(',', 2));
                    }

                    authors = record.substring(record.indexOf(',') + 1, record.indexOf(',', 2));
                    record = record.substring(record.indexOf(',', 2));
                    price = Double.parseDouble(record.substring(1, record.indexOf(',', 2)));
                    record = record.substring(record.indexOf(',', 2));
                    ISBN = record.substring(1, record.indexOf(',', 2));
                    record = record.substring(record.indexOf(',', 2));
                    genre = record.substring(1, record.indexOf(',', 2));
                    record = record.substring(record.indexOf(',', 2));
                    year = Integer.parseInt(record.substring(1));

                    //check for errors and write them in the semantic error file
                    try {
                        if (!isValidISBN(ISBN)) {
                            pw.println("\nsemantic error in file: " + fileName
                                    + "\n===================="
                                    + "\nError: invalid ISBN"
                                    + "\nRecord: " + originalRecord);
                            pw.flush();
                            if (ISBN.length() == 10) {
                                isbn10ErrorCount++;
                                throw new BadIsbnException("Error: This record's ISBN is invalid.");
                            }
                            if (ISBN.length() == 13) {
                                isbn13ErrorCount++;
                                throw new BadIsbnException("Error: This record's ISBN is invalid.");
                            }
                        } else if (!isValidPrice(price)) {
                            pw.println("\nsemantic error in file: " + fileName
                                    + "\n===================="
                                    + "\nError: invalid price"
                                    + "\nRecord: " + originalRecord);
                            priceErrorCount++;
                            pw.flush();
                            throw new BadPriceException("Error: This record's price is invalid.");
                        } else if (!isValidYear(year)) {
                            pw.println("\nsemantic error in file: " + fileName
                                    + "\n===================="
                                    + "\nError: invalid year"
                                    + "\nRecord: " + originalRecord);
                            yearErrorCount++;
                            pw.flush();
                            throw new BadYearException("Error: This record's year of publication is invalid.");
                        } else {
                            //construct new Book object and call the parametrized constructor
                            Book newBook = new Book(title, authors, price, ISBN, genre, year);

                            //declare new print writer object that will write the valid book records in each respective plain text file
                            PrintWriter genreFileWriter = null;

                            //switch statement to send each record to its appropriate location
                            switch (newBook.getGenre()) {

                                case "CCB":
                                    try {
                                        genreFileWriter = new PrintWriter(new FileOutputStream("CartoonsComics.csv.txt", true));
                                    } catch (FileNotFoundException e) {
                                        System.out.println("File not found: " + e.getMessage());
                                    }
                                    CCBbookArray[ccb] = newBook;
                                    genreFileWriter.println(CCBbookArray[ccb]);
                                    ccb++;
                                    genreFileWriter.flush();
                                    genreFileWriter.close();
                                    break;

                                case "HCB":
                                    try {
                                        genreFileWriter = new PrintWriter(new FileOutputStream("HobbiesCollectibles.csv.txt", true));
                                    } catch (FileNotFoundException e) {
                                        System.out.println("File not found" + e.getMessage());
                                    }
                                    HCBbookArray[hcb] = newBook;
                                    genreFileWriter.println(HCBbookArray[hcb]);
                                    hcb++;
                                    genreFileWriter.flush();
                                    genreFileWriter.close();
                                    break;

                                case "MTV":
                                    try {
                                        genreFileWriter = new PrintWriter(new FileOutputStream("MoviesTV.csv.txt", true));
                                    } catch (FileNotFoundException e) {
                                        System.out.println("File not found exception" + e.getMessage());
                                    }
                                    MTVbookArray[mtv] = newBook;
                                    genreFileWriter.println(MTVbookArray[mtv]);
                                    mtv++;
                                    genreFileWriter.flush();
                                    genreFileWriter.close();
                                    break;

                                case "MRB":
                                    try {
                                        genreFileWriter = new PrintWriter(new FileOutputStream("MusicRadioBooks.csv.txt", true));
                                    } catch (FileNotFoundException e) {
                                        System.out.println("File not found exception" + e.getMessage());
                                    }
                                    MRBbookArray[mrb] = newBook;
                                    genreFileWriter.println(MRBbookArray[mrb]);
                                    mrb++;
                                    genreFileWriter.flush();
                                    genreFileWriter.close();
                                    break;

                                case "NEB":
                                    try {
                                        genreFileWriter = new PrintWriter(new FileOutputStream("NostalgiaEclecticBooks.csv.txt", true));
                                    } catch (FileNotFoundException e) {
                                        System.out.println("File not found exception" + e.getMessage());
                                    }
                                    NEBbookArray[neb] = newBook;
                                    genreFileWriter.println(NEBbookArray[neb]);
                                    neb++;
                                    genreFileWriter.flush();
                                    genreFileWriter.close();
                                    break;

                                case "OTR":
                                    try {
                                        genreFileWriter = new PrintWriter(new FileOutputStream("OldTimeRadio.csv.txt", true));
                                    } catch (FileNotFoundException e) {
                                        System.out.println("File not found exception" + e.getMessage());
                                    }
                                    OTRbookArray[otr] = newBook;
                                    genreFileWriter.println(OTRbookArray[otr]);
                                    otr++;
                                    genreFileWriter.flush();
                                    genreFileWriter.close();
                                    break;

                                case "SSM":
                                    try {
                                        genreFileWriter = new PrintWriter(new FileOutputStream("SportsSportsMemorabilia.csv.txt", true));
                                    } catch (FileNotFoundException e) {
                                        System.out.println("File not found exception" + e.getMessage());
                                    }
                                    SSMbookArray[ssm] = newBook;
                                    genreFileWriter.println(SSMbookArray[ssm]);
                                    ssm++;
                                    genreFileWriter.flush();
                                    genreFileWriter.close();
                                    break;

                                case "TPA":
                                    try {
                                        genreFileWriter = new PrintWriter(new FileOutputStream("TrainsPlanesAutomobiles.csv.txt", true));
                                    } catch (FileNotFoundException e) {
                                        System.out.println("File not found exception" + e.getMessage());
                                    }
                                    TPAbookArray[tpa] = newBook;
                                    genreFileWriter.println(TPAbookArray[tpa]);
                                    tpa++;
                                    genreFileWriter.flush();
                                    genreFileWriter.close();
                                    break;
                            }
                        }
                    } catch (BadIsbnException | BadPriceException | BadYearException e) {
                        e.getMessage();
                    }
                }
            }
            recordScanner.close();
        }

        pw.println("ISBN with 10 digits errors: " + isbn10ErrorCount + " ISBN with 13 digits errors: " + isbn13ErrorCount +
                "\n" + priceErrorCount + " Invalid price errors." + "\n" + yearErrorCount + " Invalid year errors.");
        pw.println("Total errors: " + isbn10ErrorCount + isbn13ErrorCount);

        pw.flush();
        pw.close();
        sc.close();

        removeEmptyCells(CCBbookArray);
        removeEmptyCells(HCBbookArray);
        removeEmptyCells(MTVbookArray);
        removeEmptyCells(MRBbookArray);
        removeEmptyCells(NEBbookArray);
        removeEmptyCells(OTRbookArray);
        removeEmptyCells(SSMbookArray);
        removeEmptyCells(TPAbookArray);

        //serialize each array and write it in a binary file
        try {
            serialize(CCBbookArray, "Cartoons_Comics.csv.ser");
            serialize(HCBbookArray, "Hobbies_Collectibles.csv.ser");
            serialize(MTVbookArray, "Movies_TV_Books.csv.ser");
            serialize(MRBbookArray, "Music_Radio_Books.csv.ser");
            serialize(NEBbookArray, "Nostalgia_Eclectic_Books.csv.ser");
            serialize(OTRbookArray, "Old_Time_Radio_Books.csv.ser");
            serialize(SSMbookArray, "Sports_Sports_Memorabilia.csv.ser");
            serialize(TPAbookArray, "Trains_Planes_Automobiles.csv.ser");
        } catch (IOException e) {
            System.out.println("IO Exception" + e.getMessage());
        }
    }
}

