class Solution {
  public int find(int size, HashTable myTable, String word) {
    // fill this in so as to minimise collisions
    // takes in the HashTable object and the word to be found
    // the only thing you can do with the HashTable object is call check()
    // this method should return the slot in the hashtable where the word is
    
    int num = 0; // the hash index
      for (int j = 0; j < word.length(); j++) { // loop through the word to get the value for each letter
        //char letter = word.charAt(j);
        long charNum = (long)word.charAt(j); // get the ascii val of the letter
        charNum = (charNum * modPow(129, j, size)); // raise each letter to powers of 129 % size
        charNum = charNum % size;
        num += charNum; // add to total value for the word
      }

      while (!myTable.check(num, word)) {
        // not found in that slot
        int jumpSize = 13 - (num % 13);

        if (num + jumpSize <= size) { // not at the end of the array
          num += jumpSize;
        } else {
          int pos = jumpSize - (size - num) - 1; // this should wrap to the next slot in the array one jump later
          num = pos;
        }
      }

    return num;
  }

  public String[] fill(int size, String[] array) {
    // takes in the size of the hashtable, and the array of Strings to be placed in the hashtable
    // this should add all the words into the hashtable using some system
    // then it should return the hashtable array    
    String[] hashtable = new String[size];
    
    final int MAX = 13; // prime number for jump size

    for (int i = 0; i < array.length; i++) { // loop through each string in the array
      String word = array[i];
      int num = 0; // the hash index
      for (int j = 0; j < word.length(); j++) { // loop through the word to get the value for each letter
        //char letter = word.charAt(j);
        long charNum = (long)word.charAt(j); // get the ascii val of the letter
        charNum = (charNum * modPow(129, j, size)); // raise each letter to powers of 129 % size
        charNum = charNum % size;
        num += charNum; // add to total value for the word
      }

      // use double hashing for collisions
      int jumpSize = MAX - (num % MAX);

      while(hashtable[num] != null) { // if there is something in the slot
        if ((num + jumpSize) <= size) { // make sure you don't go off the end of the array
          num += jumpSize; // increase the index by the jump size
        } else { // at the end of the array
          int pos = jumpSize - (size - num) - 1; // this should wrap to the next slot in the array one jump later
          num = pos;
        }
        
      }

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

class HashTable {

  private String[] hashTable;
  private int total = 0;

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

  public int gettotal() {
    return total;
  }
}