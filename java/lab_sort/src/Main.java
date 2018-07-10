import java.io.*;
import java.lang.String;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.lang.NullPointerException;

class Main {
    public static void main(String[] args) throws IOException, NullPointerException {
        int c;
        Word wordFinal;
        Word wordBuffer;
        StringBuilder wordBuild = new StringBuilder();
        TreeSet<Word> wordStats = new TreeSet<>();
        int blocksAmount= 0;
        int wordCount = 0;
        int wordAmount;
        double buffer;

        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(args[0]));
            try {
                while ((c = reader.read()) != -1) {
                    if (Character.isLetterOrDigit(c) && (c != '\n')) {
                        wordBuild.append((char)c);
                    } else if ((c != '\n') || (wordBuild.length() != 0)) {
                        Word wordComplete = new Word(wordBuild.toString(), 1);
                        if (wordStats.contains(wordComplete)) {
                            wordBuffer = wordStats.floor(wordComplete);
                            wordStats.remove(wordComplete);
                            wordBuffer.incAmount();
                            wordStats.add(wordBuffer);
                        }
                        else {
                            wordStats.add(wordComplete);
                            blocksAmount++;
                        }
                        wordBuild.delete(0, wordBuild.length());
                    }
                }
            }
            catch (IOException eReadingLetters) {
                System.err.println("Caught error while reading letters: " + eReadingLetters.getLocalizedMessage());
            }
        }
        catch (IOException eReading) {
            System.err.println("Error while reading file: " + eReading.getLocalizedMessage());
        }
        finally {
            if (null != reader) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        try {
            File file = new File("result.csv");
            List<Word> list = new ArrayList<>(blocksAmount);
            list.addAll(wordStats);
            list.sort(new SortByAmount());
            for (int i = 0; i < blocksAmount; i++) {
                wordCount += list.get(i).getWordAmount();
            }
            try (FileWriter writer = new FileWriter(file)) {
                for (int i = 0; i < blocksAmount; i++) {
                    wordFinal = list.get(i);
                    wordAmount = wordFinal.getWordAmount();
                    writer.write(wordFinal.getWordString() +  ",");
                    writer.write(Integer.toString(wordAmount) + ",");
                    buffer = (double)wordAmount / wordCount * 100;
                    String wordResult = String.format("%.2f", buffer);
                    writer.write(wordResult + "\n");
                }
            }
            catch (IOException eWritingFile) {
                System.err.println("Caught error while writing to file: " + eWritingFile.getLocalizedMessage());
            }
        }
        catch (NullPointerException eFileNotFound) {
            System.err.println("Caught error while writing to file: " + eFileNotFound.getLocalizedMessage());
        }
    }
}

class Word implements Comparable<Word> {
    private String data;
    private int amount;

    Word(String str, int number) {
        setWordString(str);
        setWordAmount(number);
    }

    public void incAmount() {
        this.amount++;
    }

    public void setWordString(String data) {
        this.data = data;
    }

    public void setWordAmount(int amount) {
        this.amount = amount;
    }

    public String getWordString() {
        return data;
    }

    public int getWordAmount() {
        return amount;
    }

    @Override
    public int compareTo(Word word) {
        return this.getWordString().compareTo(word.getWordString());
    }
}

class SortByAmount implements Comparator<Word> {
    public int compare(Word word1, Word word2) {
        int a = word1.getWordAmount();
        int b = word2.getWordAmount();

        return b - a;
    }
}
