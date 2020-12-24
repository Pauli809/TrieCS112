package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() { }

	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null,null,null);

		if(allWords == null) { 
			return root;
		}


		for(int i = 0; i < allWords.length; i++) {
			int currentWordLengthIndex = allWords[i].length() -1;
			String currentWord = allWords[i];

			// FIRST ELEMENT CASE
			if(root.firstChild == null) { 	
				Indexes values = new Indexes(i, (short) 0, (short) currentWordLengthIndex);
				root.firstChild = new TrieNode(values, null,null); 
				continue; 
			} 
			// END OF CASE

			else { 
				TrieNode prev = root;
				TrieNode ptr = root.firstChild;
				while(ptr != null) { 
					String wordInTrie = allWords[ptr.substr.wordIndex];


					//COMMON PREFIX CASES
					if(currentWord.charAt(0) == wordInTrie.charAt(0)) {

						// CASE #1 -> NO PREVIOUS CHILDREN, FIRST COMMON PREFIX
						if(ptr.firstChild == null) { 
							int endOfPrefixIndex = -1;
							for(int j = 0; j < currentWord.length(); j++ ) {
								if(currentWord.charAt(j) == wordInTrie.charAt(j)) {
									endOfPrefixIndex++;
								}
								else {
									break;
								}
							}
							TrieNode oldSibling = ptr.sibling;
							Indexes indicesOfNewItem = new Indexes(i,(short) (endOfPrefixIndex+1), (short) currentWordLengthIndex);
							TrieNode itemToAdd = new TrieNode(indicesOfNewItem,null,null);

							ptr.substr.startIndex = (short) (endOfPrefixIndex+1);
							ptr.sibling = itemToAdd;

							Indexes indicesOfNewPrefix = new Indexes(ptr.substr.wordIndex,(short) 0,(short) endOfPrefixIndex);
							TrieNode prefixToAdd = new TrieNode(indicesOfNewPrefix,ptr,oldSibling);

							if(prev == root) {
								root.firstChild = prefixToAdd;
							}
							else { 
								prev.sibling = prefixToAdd;
							}
							break;
						} 
						// END OF CASE #1

						// CASE #2 -> COMMON PREFIX WITH A PRE-EXISTING PREFIX
						else { 	
							if(exactPrefix(ptr,allWords,currentWord)) { 

								prev = ptr;
								ptr = ptr.firstChild;
								boolean childTurn = true;
								while(ptr != null) { 
									if(allWords[ptr.substr.wordIndex].charAt(ptr.substr.startIndex) == currentWord.charAt(ptr.substr.startIndex)) {
										if(exactPrefix(ptr,allWords,currentWord)) {
											prev = ptr; 
											ptr = ptr.firstChild;
											childTurn = true;

										}
										else  { 
											break;
										}
									}
									else { 
										prev = ptr; 
										ptr = ptr.sibling;
										childTurn = false;
									}
								}
								if(ptr == null) { 
									Indexes indicesOfNewItem = new Indexes(i,(short) (prev.substr.startIndex), (short) currentWordLengthIndex);
									TrieNode itemToAdd = new TrieNode(indicesOfNewItem,null,null);
									prev.sibling = itemToAdd;
									break;
								}
								else {
									int endOfPrefixIndex = ptr.substr.startIndex;
									int start = ptr.substr.startIndex;
									for(int j = ptr.substr.startIndex+1; j < currentWord.length(); j++ ) {
										if(currentWord.charAt(j) == allWords[ptr.substr.wordIndex].charAt(j)) {
											endOfPrefixIndex++;
										}
										else {
											break;
										}
									}
									TrieNode oldSibling = ptr.sibling;
									Indexes indicesOfNewItem = new Indexes(i,(short) (endOfPrefixIndex+1), (short) currentWordLengthIndex);
									TrieNode itemToAdd = new TrieNode(indicesOfNewItem,null,null);

									ptr.substr.startIndex = (short) (endOfPrefixIndex+1);
									ptr.sibling = itemToAdd;

									Indexes indicesOfNewPrefix = new Indexes(ptr.substr.wordIndex,(short) (start),(short) endOfPrefixIndex);
									TrieNode prefixToAdd = new TrieNode(indicesOfNewPrefix,ptr,oldSibling);
									if(childTurn) { 
										prev.firstChild = prefixToAdd;
									}
									else {
										prev.sibling = prefixToAdd;
									}
									break;
								}

							}
							else {
								int endOfPrefixIndex = ptr.substr.startIndex;
								int start = ptr.substr.startIndex;
								for(int j = ptr.substr.startIndex+1; j < currentWord.length(); j++ ) {
									if(currentWord.charAt(j) == wordInTrie.charAt(j)) {
										endOfPrefixIndex++;
									}
									else {
										break;
									}
								}
								TrieNode oldSibling = ptr.sibling;
								Indexes indicesOfNewItem = new Indexes(i,(short) (endOfPrefixIndex+1), (short) currentWordLengthIndex);
								TrieNode itemToAdd = new TrieNode(indicesOfNewItem,null,null);

								ptr.substr.startIndex = (short) (endOfPrefixIndex+1);
								ptr.sibling = itemToAdd;

								Indexes indicesOfNewPrefix = new Indexes(ptr.substr.wordIndex,(short) start,(short) endOfPrefixIndex);
								TrieNode prefixToAdd = new TrieNode(indicesOfNewPrefix,ptr,oldSibling);
								if(prev == root) {
									root.firstChild = prefixToAdd;
								}
								else { 
									prev.sibling = prefixToAdd;
								}
								break;

							}

						}
						// END OF CASE
					}

					prev = ptr;
					ptr = ptr.sibling;

					// NO COMMOM PREFIX FOUND
					if(ptr == null) { 
						Indexes newItemIndices = new Indexes(i, (short)0, (short)currentWordLengthIndex);
						prev.sibling = new TrieNode(newItemIndices, null,null);
					}
				}

			}
		}
		return root;
	}


	private static boolean exactPrefix(TrieNode root,String[] allWords,String currentWord) { 
		//if(allWords[root.substr.wordIndex].substring(root.substr.startIndex, root.substr.endIndex+1).equals(currentWord.substring(root.substr.startIndex, root.substr.endIndex+1)))
		//currentWord.substring(root.substr.startIndex, root.substr.endIndex+1).contains(allWords[root.substr.wordIndex].substring(root.substr.startIndex, root.substr.endIndex+1))
		if(currentWord.substring(root.substr.startIndex).contains(allWords[root.substr.wordIndex].substring(root.substr.startIndex, root.substr.endIndex+1))) {
			return true;
		}
		else {
			return false;
		}

	}


	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
			String[] allWords, String prefix) {
		if(root == null || root.firstChild == null) { 
			return null;
		}

		ArrayList<TrieNode> list = new ArrayList<TrieNode>();

		TrieNode ptr = root.firstChild;
		String stringLeft = prefix;


		while(ptr != null) { 
			if(allWords[ptr.substr.wordIndex].charAt(0) == stringLeft.charAt(0)) {
				if(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1).contains(stringLeft)) {
					if(ptr.firstChild == null) { 
						list.add(ptr);
						return list;
					}
					else {
						deepAdd(ptr.firstChild,allWords,list,stringLeft);
						return list;
					}
				}
				else { 
					stringLeft = stringLeft.replaceFirst(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,ptr.substr.endIndex+1), "");
					return deepSearch(ptr.firstChild,allWords,list,stringLeft);
				}
			}
			ptr = ptr.sibling;
		}

		return null;
	}



	private static ArrayList<TrieNode> deepSearch(TrieNode root, String[] allWords, ArrayList<TrieNode> list, String prefix) {
		if(root == null) { 
			return list;
		}
		if(allWords[root.substr.wordIndex].charAt(root.substr.startIndex) == prefix.charAt(0)) {

			if(allWords[root.substr.wordIndex].substring(root.substr.startIndex, root.substr.endIndex+1).contains(prefix)) {
				if(root.firstChild == null) { 
					list.add(root);
					return list;
				}
				else { 
					deepAdd(root.firstChild, allWords,list,prefix);
					return list;
				}
			}
			else { 
				if(root.firstChild == null) { 
					return null;
				}
				prefix = prefix.replaceFirst(allWords[root.substr.wordIndex].substring(root.substr.startIndex,root.substr.endIndex+1), "");
				deepSearch(root.firstChild,allWords,list,prefix);
				return list;
			}
		}
		else { 
			if(root.sibling != null) {
				deepSearch(root.sibling,allWords,list,prefix);
			}
		}

		return list;
	}

	private static void deepAdd(TrieNode root, String[] allWords, ArrayList<TrieNode> list, String prefix) { 
		if(root == null) { 
			return;
		}
		if(root.firstChild == null) { 
			list.add(root);
		}
		deepAdd(root.firstChild,allWords,list,prefix);
		deepAdd(root.sibling,allWords,list,prefix);


	}













	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
					.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}

		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
}
