import java.util.Scanner;

import javax.print.attribute.IntegerSyntax;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * Program analyzes a line from a review and looks for adjectives that are in good and bad reviews. After counting how
 * many good and bad adjectives are in a review, it attributes a a star rating to the review. This program was also
 * used to take a review and switch the adjectives in it. This program comes from Project Lead the Way CSA for the
 * Consumer Review Project.
 **/
public class Review { //
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
 
  
  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * Returns a string containing all of the text in fileName (including punctuation),
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    //make sure to remove any additional space that may have been added at the end of the string.
    return temp.trim();
  }
  
  /**
   * Returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment)
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }

      /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
 
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }
  public static double totalSentiment( String filename )
  {
    try{
      String review = removePunctuation(textToString(filename));
      double sentimentValue = 0.0;
      int start =0;
      for (int i = 1; i <= review.length(); i++)
    {
      String spacingSequence = review.substring(i-1,i);
      String word = review.substring(start, i-1);
      if (spacingSequence.equals(" "))
      {
        double wordValue = Review.sentimentVal(word);
        sentimentValue += wordValue;
        start = i;
      }
    }
    return sentimentValue;
    }
    catch(Exception e)
    {
      return 0;
    }

  }
  public static Integer starRating(String fileName)
  {

    Double totalSentiment = totalSentiment(fileName);
    if(totalSentiment > 5)
    {
      return 4;
    }
    else if(totalSentiment > 0 && totalSentiment < 5)
    {
    return 3;
    }
    else if(totalSentiment > -5 && totalSentiment < 0)
    {
    return 2;
    }
    else if(totalSentiment > -10 && totalSentiment < -5)
    {
    return 1;
    }
    else
    {
    return 0;
    }
}
public static String fakeReview(String fileName, String GB)
{
  String review = removePunctuation(textToString(fileName));
  int start = 0;
  String fakeReview = review;
  for (int i = 1; i <= review.length(); i++)
  {
    String spacingSequence = review.substring(i-1,i);
    String word = review.substring(start, i-1);
    String newWord = "";
    if (spacingSequence.equals(" "))
    {
      if (review.substring(start, start+1).equals("*"))
      {
        if (GB.equals("GoodFake"))
        {
          String[] posAdjectives = {"good", "amazing", "outstanding", "great", "creative", "diligent", "fast", "energetic", "friendly", "funny", "generous", "hilarious"};
          Random random = new Random();
          int posIndex = random.nextInt(posAdjectives.length);
          newWord = posAdjectives[posIndex];
        }
        else if (GB.equals("BadFake"))
        {
          String[] negAdjectives = {"bad", "mean", "rude", "bossy","careless", "cynical", "dishonest", "grumpy", "harsh", "impatient", "impolite", "awful", "creepy"};
          Random random = new Random();
          int negIndex = random.nextInt(negAdjectives.length);
          newWord = negAdjectives[negIndex];
        }
        fakeReview = fakeReview.replaceAll(removePunctuation(word), newWord);
      }
      start = i;
    }
  }
  return fakeReview.replace("*", "");
}
public static String Count(String fileName){
  String review = textToString(fileName);
  int start = 0;
  Integer negativecount = 0;
  Integer positivecount = 0;
  //Runs the totalSentiment method to get a Double
  Double totalSentiment = totalSentiment(fileName);
  //Runs the starRating method to get a Integar
  Integer starRating = starRating(fileName);
  for (int i = 1; i <= review.length(); i++)
  {
    String spacingSequence = review.substring(i-1,i);
    String word = review.substring(start, i-1);
    if (spacingSequence.equals(" "))
    {
      double wordValue = sentimentVal(word);
      
      if (wordValue < 0)
      {
        //Analyzes value of the word. If the words value is below a 1, it adds 1 to the negative count.
          negativecount += 1;
      }
      else if (wordValue > 0)
      {
        //Analyzes value of the word. If the words value is above a 1, it adds 1 to the positive count.
          positivecount =+ 1;
      }
      start = i;
    }
  }
  // Gathers the count of positve and negative comments, the total sentiment and star rating and places them all into
  // a single list.
  String[] count = new String[4];
  count[0] = Integer.toString(positivecount);
  count[1] = Integer.toString(negativecount);
  count[2] = Double.toString(totalSentiment);
  count[3] = Integer.toString(starRating);
  // Prints a statement about the review overall with items in the list above.
  return ("This review has " + count[0] + " postive adjectives, with " + count[1] + " negative adjectives, has a total sentiment of " + count[2] + " is rated " + count[3] + " stars.");
}
  public static void main(String[] args {
    System.out.println(fakeReview("SimpleReview.txt","GoodFake"));
  }
}
