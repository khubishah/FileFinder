
import java.util.HashMap;
import java.io.*;


class FindFiles {
    static HashMap<String, String> arguments = new HashMap<>();
    static String fileToFind = "";
    static boolean atLeastOneFound = false;

    private static void displayMenu() {
        System.out.println("  Usage: java FindFiles filetofind [-option arg]\n" +
                "  -help                     :: print out a help page and exit the program.\n" +
                "  -r                        :: execute the command recursively in subfiles.\n" +
                "  -reg                      :: treat `filetofind` as a regular expression when searching.\n" +
                "  -dir [directory]          :: find the files starting in the specified directory named [directory]. \n" +
                "  -ext [ext1,ext2,...]      :: find the files matching [filetofind] and with the given extensions [ext1, ext2,...]");
        System.exit(0);
    }

    private static void printError(String errorType, boolean printMenu) {
        System.out.println(errorType);
        if (printMenu) displayMenu();
        System.exit(0);
    }

    private static void createArgsHashmap(String[] args) {
        int n = args.length;
        int i = 1;
        while (i < n) {
            if (args[i].equals("-help")) {
                displayMenu();
            } else if (args[i].equals("-r") || args[i].equals("-reg")) {
                if (args[i].equals("-r")) {
                    arguments.put(args[i], "");
                }
                else {
                    arguments.put(args[i], fileToFind);
                }
                i++;
            } else if (args[i].equals("-dir") || args[i].equals("-ext")) {
                if (i + 1 == n || args[i+1].startsWith("-")) {
                    if (args[i].equals("-dir")) {
                        printError("Please provide an absolute or relative directory path as a parameter for the -dir extension.", false);
                    } else {
                        printError("Please provide one or more extensions as a parameter to -ext in the form \"ext1,ext2,ext3...\".", false);
                    }


                }
                else {
                    arguments.put(args[i], args[i+1]);
                    i+=2;
                }
            } else {
                printError("Invalid command syntax. " + args[i] + " is not a valid option. Please see the help menu below.", true);
            }
        }

    }


    private static void extractFileName(String[] args) {
        if (args.length == 0) {
            displayMenu();
        } else {
            if (args[0].startsWith("-")) {
                printError("Please provide a valid filename or regex expression as the first argument.", true);
            }
            else {
                fileToFind = args[0];
            }
        }
    }

    private static boolean checkExtensions(String filename, boolean isRegex) {
        String [] extensions = arguments.get("-ext").split(",");

        for (int i = 0; i < extensions.length; i++) {
            if (isRegex) {
                if (filename.endsWith("." + extensions[i])) return true;
            } else {
                if ((fileToFind + "." + extensions[i]).equals(filename)) return true;
            }
        }
        return false;
    }

    private static void walkTree(File dir) {
        boolean isRecursive = arguments.containsKey("-r");
        boolean isRegEx = arguments.containsKey("-reg");
        boolean matchExtensions =  arguments.containsKey("-ext");
        File [] listFile = dir.listFiles();
        boolean passed = true;

        if (listFile != null) {
            if (!isRegEx) {
                System.out.print("Looking in directory " + dir.getName() + " for a file named " + fileToFind);
            } else {
                System.out.print("Looking in directory " + dir.getName() + " for files matching the regular expression " + fileToFind);
            }
            if (matchExtensions) {
                System.out.println(" with extensions (" + arguments.get("-ext") + ")...");
            } else {
                System.out.println(".");
            }

            for (int i=0; i< listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    if (isRecursive) {
                        walkTree(listFile[i]);
                    }
                } else {
                    if (isRegEx) {
                        try {
                            if (!listFile[i].getName().matches(arguments.get("-reg"))) {
                                passed = false;
                            }
                        } catch (Exception x) {
                            printError("Please provide a valid regex expression.", false);
                        }

                    }
                    if (matchExtensions) {
                        boolean matchedExt = checkExtensions(listFile[i].getName(), isRegEx);
                        if (!matchedExt) {
                           passed = false;
                        }
                    }
                    if (!isRegEx && !matchExtensions) {
                        if (!listFile[i].getName().equals(fileToFind)) {
                            passed = false;
                        }
                    }

                    if (passed == true) {
                        System.out.println("File found! The absolute path:");
                        System.out.println(listFile[i].getPath());
                        atLeastOneFound = true;
                    } else {
                        passed = true;
                    }
                }
            }
        }
    }

    private static void findFile() {
        String initialDirectory =  arguments.getOrDefault("-dir", "");
        File f;
        if (initialDirectory.equals("")) {
            f = new File(System.getProperty("user.dir"));
        } else if (initialDirectory.startsWith("/")) {
            f = new File(initialDirectory);
        } else {
            f = new File(System.getProperty("user.dir") + initialDirectory.substring(1));
        }
        //File f = new File((initialDirectory.equals("") ? System.getProperty("user.dir") : initialDirectory ));
        if (!f.exists()) {
            printError("Please enter a valid directory path.", false);
        }
        walkTree(f);
        System.out.println("File search completed.");
        if (!atLeastOneFound) {
            System.out.println("No matching files were found.");
        }
    }

    public static void main(String[] args) {
        extractFileName(args);
        createArgsHashmap(args);
        findFile();

    }
}
