import java.util.*;

public class HuffmanCode
{
  public static void main(String[] args)
  	{
    	Scanner Sentence = new Scanner(System.in);
    	System.out.print("Enter a text: ");
    	String text = Sentence.nextLine();
    	int length = text.length();
    	int[] counts = getCharacterFrequency(text); // find the frequency of each character and place it in the (ascii)index of array
    	Tree tree = getHuffmanTree(counts); // make the tree with a priority queue
    	String[] codes = getCode(tree.root); // generate code for each character based on its frequency and so place in the tree
		String binaryString="";
		System.out.println("\nCharacter"+"\t"+"Binary"+"\t"+"New Code");//print the old code and the new code
    	for (int i = 0; i < codes.length; i++)
    	{
    		if (counts[i] != 0) // the character does'nt appear in the string if its frequency is 0
      		{
            	String binaryValue = Integer.toBinaryString(i);
            	for(int j=7;j>binaryValue.length();j--)
            	{
               		binaryString+="0";           //this loop adds in those pesky leading zeroes
            	}
           		binaryString += binaryValue+" "; //add to the string of binary
       			System.out.println((char)i+"\t\t\t" +binaryString +"\t"+codes[i]);
       			binaryString="";
      		}
    	}

      	System.out.println("\nCharacter"+"\t"+"Frequency");//prints out character frequency
      	for(int i3 =text.length(); i3>0 ; i3--)
      	{

      		for (int i2 = 0; i2 < codes.length; i2++)
      		{
      			if (counts[i2] != 0 && counts[i2]==i3)
      			{
       				System.out.println((char)i2+"\t\t\t" +counts[i2] +"\t\t");
      			}
      		}
      	}

		System.out.println("\nOriginal String in binary\n");//prints original string in binary
      	for(int a = 0; a < text.length(); a++)
      	{
      		int number = (int)text.charAt(a);
            String binary = Integer.toBinaryString(number);//converts the characters of the words to binary
            System.out.print(binary+" ");
      	}

		System.out.println("\n\nOriginal String in Huffman code\n");//prints original string in newly generated Huffman code
      	for(int b = 0; b < text.length(); b++)
      	{
      		int number = (int)text.charAt(b);
            String code = codes[number];
            System.out.print(code +" ");
      	}
      	int binaryBytes = 0;
      	int huffmanBytes = 0;
      	double compression = 0.0;
      	for(int x=0; x < counts.length; x++)
      	{
			if(counts[x]!=0)
			{
				huffmanBytes += codes[x].length()*counts[x];
			}
      	}
      	binaryBytes += length*7;
      	System.out.println("\nNumber of bits in binary: "+binaryBytes);
      	System.out.println("Number of bits in huffman code: "+huffmanBytes);
      	compression= ((double)huffmanBytes/binaryBytes)*100;
      	System.out.printf("The compression rate is: %.2f", compression);
      	System.out.print("%");
  }

  //this method is called once the tree is created to give the characters huffman codes
  public static String[] getCode(Tree.Node root)
  {

    	if (root == null)//if the root is empty there is no code to create
    	{
    		return null;
    	}
    	//for each ascii position which has a frequecy>=1 the huffman code will be stored here
    	String codes[] = new String[256];
    	assignCode(root, codes);
    	int charOccur=0;
    	//if there is only one character then it will be given the code of 0
		for(int i=0;i<codes.length;i++)
		{
			if(codes[i]!=null)
				charOccur++;
		}
		if(charOccur==1)
		{
			codes[(int)root.element]="0";
		}
    	return codes;
  }

  /* Recursively get codes to the leaf node */
  private static void assignCode(Tree.Node root, String[] codes)
  	{
   		if (root.leftchild != null)//keep going so long as there are more trees with characters in them
   		{
      		root.leftchild.code = root.code + "0";//the node to the left will be smaller than th right
      		assignCode(root.leftchild, codes);

      		root.rightchild.code = root.code + "1";//the node to the right will have a greater frequency
      		assignCode(root.rightchild, codes);
    	}
    	else
    	{
    	 	codes[(int)root.element] = root.code;//the character stored in the node's ascii value index
    	}
  	}

  //make a huffman tree based on frequency of chars
  public static Tree getHuffmanTree(int[] counts)
  	{
    	// Create a heap to hold trees
    	PriorityQueue<Tree> Map = new PriorityQueue<Tree>();
    	for (int i = 0; i < counts.length; i++)
    	{
      		if (counts[i] > 0)
        	Map.add(new Tree(counts[i], (char)i)); // A leaf node tree, each newly insert node will be sorted according to the comparable method (compareto) below
    	}

    while (Map.size() > 1)
    {//the nodes and trees are sorted in the priority queue
      	Tree t1 = Map.remove(); // remove the smallest frequency tree
      	Tree t2 = Map.remove(); // remove the next smallest frequency tree
      	Map.add(new Tree(t1, t2)); // combine the two trees
    }

    	return Map.remove(); // at the end all the original trees will have been combined
  	}

  	// get the frequency of the characters
  	public static int[] getCharacterFrequency(String text)
  	{
    	int[] counts = new int[256]; // 256 ASCII characters

    	for (int i = 0; i < text.length(); i++)
    	{
      		counts[(int)text.charAt(i)]++; //count the occurences of the character in text
    	}

    	return counts;
  	}


  	public static class Tree implements Comparable<Tree>
  	{
    	Node root; // the root of the tree

    	// create a tree with two subtrees
    	public Tree(Tree t1, Tree t2)
    	{
      		root = new Node();
      		root.leftchild = t1.root;
      		root.rightchild = t2.root;
      		root.frequency = t1.root.frequency + t2.root.frequency;
    	}
    	//create a tree containing a leaf node
    	public Tree(int frequency, char element)
    	{
      		root = new Node(frequency, element);
   		}
   		//This method is used by the tree class to compare the frequency of nodes elements
   		public int compareTo(Tree t)
   		 {
      		if (root.frequency > t.root.frequency)
      		{
        		return 1;
      		}
      		else if (root.frequency == t.root.frequency)
      		{
        		return 0;
   		 	}
      		else
      		{
        		return -1;
      		}
    	}

   	 	public class Node
    	{
      		char element; // stores the character for a leaf node
      		int frequency; // frequency of the subtree rooted at this node
      		Node leftchild; // reference to the left subtree
      		Node rightchild; // reference to the right subtree
      		String code = ""; // the code of this node from the root

      		//create an empty node
      		public Node()
      		{
      		}
      		// create a node with the specified weight and character
      		public Node(int frequency, char element)
      		{
       	 	this.frequency = frequency;
        	this.element = element;
      		}
    	}
  	}
}