
import java.util.*;
import java.math.*;
import java.math.BigInteger;
import java.lang.Object.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Stub {

  public static void main(String[] args) {
    Scanner myscanner = new Scanner(System.in);
    int items = myscanner.nextInt() - 1;
    myscanner.nextLine();
    String[] contents = new String[items];
    for (int i = 0; i < items; i++) {
      contents[i] = myscanner.nextLine();
    }
    String hash = myscanner.nextLine();
    int size = 99991;
    Solution mysolution = new Solution();
    String[] hashtable = mysolution.fill(size, contents);
    HashTable mytable = new HashTable(hashtable);

    Solution mysolution2 = new Solution(); // prevents cheating by using memory
    for (int i = 0; i < items; i++) {
      int rand = (int) (Math.random() * items);
      String temp = contents[i];
      contents[i] = contents[rand];
      contents[rand] = temp;
    }
    int total = 0;
    for (int i = 0; i < items; i++) {
      int slot = mysolution2.find(size, mytable, contents[i]);
      if (!hashtable[slot].equals(contents[i])) {
        System.out.println("error!");
      }
    }
    System.out.println("Collisions: " + mytable.gettotal());
    try {
      System.out.println("Here is your receipt: " + sha256(hash + mytable.gettotal()));
    } catch (NoSuchAlgorithmException e) {
    }
    ;
  }

  public static String sha256(String input) throws NoSuchAlgorithmException {
    byte[] in = hexStringToByteArray(input);
    MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
    byte[] result = mDigest.digest(in);
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    if (len % 2 == 1) {
      s = s + "@";
      len++;
    }
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }
}

class HashTable {

  private String[] hashTable;
  private long total = 0;

  public HashTable(String[] input) {
    hashTable = input;
  }

  public boolean check(int slot, String check) {
    if (hashTable[slot].equals(check)) {
      return true;
    } else {
      total++;
      return false;
    }
  }

  public long gettotal() {
    return total;
  }
}

class Solution {

  public int find(int size, HashTable mytable, String word) {

    // fill this in so as to minimize collisions
    // takes in the HashTable object and the word to be found
    // the only thing you can do with the HashTable object is call "check"
    // this method should return the slot in the hashtable where the word is
    
    //size = size * 2; // double this, no longer a prime though?? 
    
    int num = 0; // the hash index
    for (int j = 0; j < word.length(); j++) { // loop through the word to get the value for each letter
      // char letter = word.charAt(j);
      long charNum = (long) word.charAt(j); // get the ascii val of the letter
      charNum = (charNum * modPow(129, j, size)); // raise each letter to powers of 129 % size
      
      // long power = (long)Math.pow(129,j);
      // power = power % size;
      // charNum = charNum * power;
      
      charNum = charNum % size;
      num += charNum; // add to total value for the word
      num = num % size;
    }

    int jumpSize = 7 - (num % 7);

    while (!mytable.check(num, word)) {
      // not found in that slot

      if (num + jumpSize < size) { // not at the end of the array
        num += jumpSize;
      } else {
        num = jumpSize - (size - num); // this should wrap to the next slot in the array one jump later
      }
    }
    /*
     * while (!mytable.check(num, word)) { // if there is something in the slot if
     * (num == size-1) { // make sure you don't go off the end of the array num = 0;
     * // increase the index by the jump size } else { // at the end of the array
     * num += 1; }
     * 
     * }
     */
    return num;
  }

  public String[] fill(int size, String[] array) {
    // System.out.println("Size: " + size);

    // takes in the size of the hashtable, and the array of Strings to be placed in
    // the hashtable
    // this should add all the words into the hashtable using some system
    // then it should return the hashtable array

    //size = size * 2; // double the array length, no longer prime though
    
    String[] hashtable = new String[size];
    // final int MAX = 7; // prime number for jump size

    for (int i = 0; i < array.length; i++) { // loop through each string in the array
      String word = array[i];
      int num = 0; // the hash index
      for (int j = 0; j < word.length(); j++) { // loop through the word to get the value for each letter
        // char letter = word.charAt(j);
        long charNum = (long) word.charAt(j); // get the ascii val of the letter
        charNum = (charNum * modPow(129, j, size)); // raise each letter to powers of 129 % size
        
        // long power = (long)Math.pow(129,j);
        // power = power % size;
        // charNum = charNum * power;
        
        charNum = charNum % size;
        num += charNum; // add to total value for the word
        num = num % size;
      }

      // use double hashing for collisions
      int jumpSize = 7 - (num % 7);
      // System.out.println("jumpSize: " + jumpSize);

      while (hashtable[num] != null) { // if there is something in the slot
        if ((num + jumpSize) < size) { // make sure you don't go off the end of the array
          num += jumpSize; // increase the index by the jump size
        } else { // at the end of the array
          num = jumpSize - (size - num); // this should wrap to the next slot in the array one jump later

        }

      }

      /*
       * while(hashtable[num] != null) { // if there is something in the slot if (num
       * == size-1) { // make sure you don't go off the end of the array num = 0; //
       * increase the index by the jump size } else { // at the end of the array num
       * += 1; }
       * 
       * }
       */

      hashtable[num] = word; // insert the word in the empty slot

    }
    return hashtable;

  }

  public long modPow(long number, long power, long modulus) {
    // raises a number to a power with the given modulus

    if (power == 0)
      return 1;
    else if (power % 2 == 0) {
      long halfpower = modPow(number, power / 2, modulus);
      return modMult(halfpower, halfpower, modulus);
    } else {
      long halfpower = modPow(number, power / 2, modulus);
      long firstbit = modMult(halfpower, halfpower, modulus);
      return modMult(firstbit, number, modulus);
    }
  }

  public long modMult(long first, long second, long modulus) {
    // multiplies the first number by the second number with the given modulus

    if (second == 0)
      return 0;
    else if (second % 2 == 0) {
      long half = modMult(first, second / 2, modulus);
      return (half + half) % modulus;
    } else {
      long half = modMult(first, second / 2, modulus);
      return (half + half + first) % modulus;
    }
  }
}