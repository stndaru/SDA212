package TP2;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Target;
import java.util.*;

public class TP2v1 {
    private static InputReader in;
    private static PrintWriter out;

    /*
        DATA STRUCTURE:

            KUIL REFERENCES PULAU
            DATARAN REFERENCES KUIL
            DATARAN DOESN'T ACTUALLY CONNECT BETWEEN KUIL
            WHEN TRAVERSE TO DIFFERENT KUIL, CHECK KUIL AND TRAVERSE KUIL DLL

            Kuil stored in HashMap Kuil {Kuil :  PulauName}
            Pulau also stored in HashMap Pulau {Pulau : PulauObj} 

            Dataran are LinkedList
            LinkedList for ordering based off order from prev to next
            
            Pulau Object:
                String Nama
                String kuilNameFirst - The first Kuil of the Island
                HashMap {KuilName : KuilObject}
                Kuil head
                Kuil tail

            Kuil Object:
                AVL Tree[Dataran] sorted based on Height
                Dataran head
                Dataran tail
                Kuil prev
                Kuil next

            Dataran Object:
                CustomInteger height REFERENCE AVLTreeNode.height
                Dataran prev
                Dataran next
                boolean isKuil
                String kuilName

            AVLTree:
                Stores Object Dataran using seperate LinkedList
                Stores CustomInteger heightValue
                Tree sorted by Dataran Height

            RAIDEN SHOGUN:
            Class pointer yang memiliki atribut 
                -Kuil saat ini  
                -Dataran saat ini (tinggi dan dataran keberapa)


        UNIFIKASI U V - Combine V to U - Plays with Kuil
            CORE: Get head of Pulau V, attach head to tail of U, update U tail, add all Kuil to Hashmap, delete V

            Get Pulau U and Pulau V
            U.tail.next = V.head
            V.head.prev = U.tail
            U.tail = V.tail
            for i : keySet in V.kuilMap
                U.kuilMap.put(i, V.kuilMap.get(i))
            Pulau.remove(V)

        PISAH U - Remove U from V - Plays with Kuil
            CORE: Get Pulau where U reside, get kuil U, create new Pulau U, assign kuil U as U.head, assign P.tail as U.tail

            Get Pulau V from HashMap Kuil
            new Pulau(U)
            pulauMap.put(U : Uobj)
            U.head = V.kuilMap.get(U)
            U.tail = V.tail
            V.tail = kuilU.next
            V.tail.next = null
            U.head.prev = null
            tempKuil = U.head
            while tempKuil != null
                U.kuilMap.put(tempKuil.nama : tempKuil)
                V.kuilMap.remove(tempKuil.nama)
                tempKuil.next

        GERAK - Traverse Dataran, Kuil
            CORE: Get Raiden's location, dataran = dataran.next/prev. if Null traverse Kuil DLL, if exist next/prev Kuil, update Kuil and go to head/tail Dataran. 
                    Get dataran height.

        TEBAS - Traverse Dataran, Kuil
            CORE: get Kuil info, get Dataran height, traverse AVLTree, go inside and traverse until found Dataran = Dataran (compare object)
                    go prev/next depends and using counter, if prev/next is null, 
                    go to Kuil.next/prev depends, traverse AVLTree, if not exist height, continue traverse Kuil 
                    and repeat but start from tail/head

        TELEPORTASI - Traverse Dataran, Kuil, Pulau
            CORE: Just get the Kuil info and assign RS.Dataran to the Kuil's Dataran (the Head)

        RISE U H X
            CORE: Iterate Kuil, Iterate AVLTree, update heightValue (Dataran.height would get updated because pass by-reference)

        QUAKE U H X - Possibly O(nm) n for iterating Dataran, m for iterating AVLTree
            NOTE: Check for case if height intersect with other Node (QUAKE until minimum default value of 1), if yes will have to combine list
            Combining can use a ton of time, traverse AVL tree, find all Node with final height of 1, delete
            From kuilStart, iterate from beginning to create new Node valued 1 and update the Dataran's height (to preserve Order)
            
            CORE:

        CRUMBLE
            CORE: Get Kuil and Dataran info, check if dataran Kuil or not, if not, Store DataranObj, GERAK L
                    Check if Dataran tail or not, if Yes, update Kuil.tail and nextKuil.head
                    Update Dataran DLL
                    Go to AVLTree, find Dataran, delete and update Node DLL, check if DLL empty or not, if empty delete Node

        STABILIZE 
            CORE: Get Kuil and Dataran info, check if dataran Kuil or not, if not, check prev is higher or not
                    if not, create new Dataran, update Dataran DLL and insert to AVLTree

        SWEEPING
            CORE: Get Pulau, Traverse Kuil, for every Kuil traverse AVL Tree with 
            Receive input Long (limit is 10^12)
    
    */

    /* 
        CURRENTLY KNOWN ISSUE
        RTE - too much memory OR null pointer somewhere
        Maybe tone down on some data structures?
        kuilMapGeneral can be deleted and use the kuilMap within Pulau to check because this will double the memory of Kuil
        but as a change will probably result in heavy amount of TLE because O(n) where n amount of Pulau
    
    */

    static HashMap<String, String> kuilMapGeneral = new HashMap<String, String>();
    static HashMap<String, Pulau> pulauMap = new HashMap<String, Pulau>();
    static RaidenShogun raidenShogun;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Island and Kuil generation
        int N = in.nextInt();
        for(int i = 0; i < N; i++){
            String nama = in.next();
            pulauMap.put(nama, new Pulau(new Kuil(nama)));

            int dataranAmount = in.nextInt();

            int firstDataranHeight = in.nextInt();
            pulauMap.get(nama).getKuil(nama).headDataran(firstDataranHeight);
            kuilMapGeneral.put(nama, nama);

            for(int j = 0; j < dataranAmount-1; j++){
                int dataranHeight = in.nextInt();
                pulauMap.get(nama).getKuil(nama).addDataran(dataranHeight);
            }
        }

        // RaidenShogun location
        String nama = in.next();
        int dataran = in.nextInt();
        raidenShogun = new RaidenShogun(pulauMap.get(nama).getKuil(nama), dataran);

        // Starting input
        N = in.nextInt();
        for(int i = 0; i < N; i++){
            String command = in.next();

            String direction;
            int amount;
            int heightChange;

            switch(command){

                case "UNIFIKASI":
                    String pulauDepan = in.next();
                    String pulauBelakang = in.next();
                    out.println(Unifikasi(pulauMap.get(pulauDepan), pulauMap.get(pulauBelakang)));
                    break;

                case "PISAH":
                    String pulauBaru = in.next();
                    out.println(Pisah(pulauMap.get(kuilMapGeneral.get(pulauBaru)).getKuil(pulauBaru), pulauMap.get(kuilMapGeneral.get(pulauBaru))));
                    break;

                case "GERAK":
                    direction = in.next();
                    amount = in.nextInt();
                    out.println(Gerak(direction, amount));
                    break;

                case "TEBAS":
                    direction = in.next();
                    amount = in.nextInt();
                    out.println(Tebas(direction, amount));
                    break;

                case "TELEPORTASI":
                    direction = in.next();
                    out.println(Teleportasi(pulauMap.get(kuilMapGeneral.get(direction)).getKuil(direction)));
                    break;

                case "RISE":
                    direction = in.next();
                    amount = in.nextInt();
                    heightChange = in.nextInt();
                    out.println(Rise(pulauMap.get(direction), amount, heightChange));
                    break;
                
                case "QUAKE":
                    direction = in.next();
                    amount = in.nextInt();
                    heightChange = in.nextInt();
                    out.println(Quake(pulauMap.get(direction), amount, heightChange));
                    break;

                case "CRUMBLE":
                    out.println(Crumble());
                    break;

                case "STABILIZE":
                    out.println(Stabilize());
                    break;

                case "SWEEPING":
                    String pulauName = in.next();
                    int heightRisen = in.nextInt();
                    out.println(Sweeping(pulauMap.get(pulauName), heightRisen));
                    break;

                case "SANITY":
                    Sanity();
                    break;

                case "STATUS":
                    raidenShogun.getCurrentStatus();
                    break;

                default:
                    break;
            }
        }

        out.flush();
    }

    static int Unifikasi(Pulau depan, Pulau belakang){
        int totalDataran = 0;

        depan.tail.next = belakang.head;
        belakang.head.prev = depan.tail;
        depan.tail = belakang.tail;

        for(String key : belakang.kuilMap.keySet()){
            depan.kuilMap.put(key, belakang.kuilMap.get(key));
            kuilMapGeneral.put(key, depan.nama);
        }

        for(Kuil i : depan.kuilMap.values()){
            totalDataran += i.totalDataran;
        }

        pulauMap.remove(belakang.nama);

        return totalDataran;
    }

    static String Pisah(Kuil kuil, Pulau oldPulau){
        // Cases to consider - Only 2 Kuil

        pulauMap.put(kuil.nama, new Pulau(kuil));
        kuilMapGeneral.put(kuil.nama, kuil.nama);
        Pulau newPulau = pulauMap.get(kuil.nama);
        
        newPulau.tail = oldPulau.tail;
        oldPulau.tail = newPulau.head.prev;
        oldPulau.tail.next = null;
        newPulau.head.prev = null;

        Kuil temp = newPulau.head.next;
        oldPulau.kuilMap.remove(newPulau.head.nama);

        while(temp != null){
            newPulau.kuilMap.put(temp.nama, temp);
            oldPulau.kuilMap.remove(temp.nama);
            kuilMapGeneral.put(temp.nama, newPulau.nama);
            temp = temp.next;
        }

        int oldPulauTotalDataran = 0;
        int newPulauTotalDataran = 0;

        temp = oldPulau.head;
        while(temp != null){
            oldPulauTotalDataran += temp.totalDataran;
            temp = temp.next;
        }

        temp = newPulau.head;
        while(temp != null){
            newPulauTotalDataran += temp.totalDataran;
            temp = temp.next;
        }

        return oldPulauTotalDataran + " " + newPulauTotalDataran;
    }

    static int Gerak(String direction, int amount){
        return raidenShogun.gerak(direction, amount);
    }

    static int Tebas(String direction, int amount){
        return raidenShogun.tebas(direction, amount);
    }

    static int Teleportasi(Kuil target){
        return raidenShogun.teleportasi(target);
    }

    static int Rise(Pulau pulau, int height, int heightIncrease){
        return pulau.rise(height, heightIncrease);
    }

    static int Quake(Pulau pulau, int height, int heightIncrease){
        return pulau.quake(height, heightIncrease);
    }

    static int Crumble(){
        Kuil currentKuil = raidenShogun.currentKuil;
        Dataran currentDataran = raidenShogun.currentDataran;
        int height = currentDataran.height.value;

        if(currentDataran.isKuil) return 0;

        raidenShogun.currentDataran = raidenShogun.currentDataran.prev;
        
        // Case if raiden on tail
        if(currentKuil.tail == currentDataran){
            currentKuil.tail = currentDataran.prev;
            currentKuil.tail.next = null;
        }
        else{
            currentDataran.next.prev = currentDataran.prev;
            currentDataran.prev.next = currentDataran.next;
        }

        currentKuil.dataranTree.delete(height, currentDataran);

        currentKuil.totalDataran -= 1;

        return height;
    }

    static int Stabilize(){
        Kuil currentKuil = raidenShogun.currentKuil;
        Dataran currentDataran = raidenShogun.currentDataran;
        Dataran targetDataran;
        Dataran tempDataran;
        AVLTreeNodeDLL tempNodeDLL;

        if(currentDataran.isKuil) return 0;

        targetDataran = (currentDataran.prev.height.value > currentDataran.height.value) ? currentDataran : currentDataran.prev;
        Dataran newDataran = new Dataran(targetDataran.height, targetDataran.kuilName);
        
        if(currentDataran.next != null){
            tempDataran = currentDataran.next;
            currentDataran.next = newDataran;
            tempDataran.prev = newDataran;
            newDataran.next = tempDataran;
            newDataran.prev = currentDataran;

            Node treeNode = currentKuil.dataranTree.find(targetDataran.height.value);
            AVLTreeNodeDLL treeNodeDLL =  treeNode.findDataranNode(targetDataran);

            if(treeNodeDLL.next != null){
                tempNodeDLL = treeNodeDLL.next;
                treeNodeDLL.next = new AVLTreeNodeDLL(newDataran);
                tempNodeDLL.prev = treeNodeDLL.next;
                treeNodeDLL.next.next = tempNodeDLL;
                treeNodeDLL.next.prev = treeNodeDLL;
            }
            else {
                treeNodeDLL.next = new AVLTreeNodeDLL(newDataran);
                treeNodeDLL.next.prev = treeNodeDLL;
                treeNode.tail = treeNodeDLL.next;
            }
            treeNode.totalDataranInNode += 1;

        }
        else {
            currentDataran.next = newDataran;
            currentDataran.next.prev = currentDataran;
            currentKuil.tail = newDataran;

            Node treeNode = currentKuil.dataranTree.find(targetDataran.height.value);
            treeNode.tail.next = new AVLTreeNodeDLL(newDataran);
            treeNode.tail = treeNode.tail.next;
            treeNode.totalDataranInNode += 1;
        }

        return targetDataran.height.value;
    }

    static int Sweeping(Pulau pulau, int heightRisen){
        return pulau.sweeping(heightRisen);
    }

    static void Sanity(){
        out.println();
        sanityCheck();
        raidenShogun.getCurrentStatus();
        out.println("================");
        out.println();
    }

    static void sanityCheck(){
        // out.println(pulauMap);
        // out.println(kuilMapGeneral);
        for(Pulau i : pulauMap.values()){
            out.println("\n== GANTI PULAU ==");
            Kuil temp = i.head;
            while(temp != null){
                out.print(temp.nama + " ");
                temp.sanityCheckKuil();
                out.println();
                temp = temp.next;
            }
        }
        out.println("==== FINISHED ====");
    }


    // Integer but able to be passed by reference
    static class CustomInteger{
        public int value;

        public CustomInteger(int value) {
            this.value = value;
        }
        
        @Override
        public String toString(){
            return String.valueOf(this.value);
        }

        public void increment(int inc){
            this.value += inc;
        }

        public void decrement(int dec){
            this.value -= dec;
        }

        public void setValue(int val){
            this.value = val;
        }
    }

    // taken from https://codeforces.com/submissions/Petr
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;
 
        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
 
        public int nextInt() {
            return Integer.parseInt(next());
        }
 
    }

    static class Pulau {

        String nama;
        String kuilNamaFirst;
        HashMap<String, Kuil> kuilMap = new HashMap<String,Kuil>();

        Kuil head;
        Kuil tail;
        Kuil temp;

        public Pulau(Kuil kuil){
            this.nama = kuil.nama;
            this.kuilNamaFirst = nama;
            this.head = kuil;
            this.tail = kuil;
            kuilMap.put(nama, kuil);
        }

        public Kuil getKuil(String nama){
            return kuilMap.get(nama);
        }

        public int sweeping(int heightRisen){
            int count = 0;
            temp = head;
            while(temp != null){
                count += temp.dataranTree.sweepingTraversal(heightRisen);
                temp = temp.next;
            }
            return count;
        }

        //
        public int rise(int height, int heightRisen){
            int count = 0;
            temp = head;
            while(temp != null){
                count += temp.dataranTree.riseTraversal(height, heightRisen);
                temp = temp.next;
            }
            return count;
        }

        public int quake(int height, int heightRisen){
            int count = 0;
            temp = head;
            while(temp != null){
                count += temp.dataranTree.quakeTraversal(height, heightRisen);
                temp = temp.next;
            }
            return count;
        }
    }

    static class Kuil {
        String nama;
        DataranAVLTree dataranTree = new DataranAVLTree();

        Dataran head;
        Dataran tail;
        Dataran temp;

        Kuil prev;
        Kuil next;

        int totalDataran;

        public Kuil(String nama){
            this.nama = nama;
        }

        public void sanityCheckKuil(){
            temp = head;
            while(temp != null){
                out.print(temp.height + " ");
                temp = temp.next;
            }
            out.print("/" + this.totalDataran + "\n");
            dataranTree.inOrderTraversal();
        }

        public void headDataran(int heightVal){
            Node treeNode = dataranTree.insert(heightVal);
            Dataran dataran = new Dataran(treeNode.heightValue, nama, true);

            treeNode.addDataran(dataran);

            this.head = dataran;
            this.tail = dataran;
            this.totalDataran = 1;
        }

        public void addDataran(int heightVal){
            Node treeNode = dataranTree.insert(heightVal);
            Dataran dataran = new Dataran(treeNode.heightValue, nama);
            
            treeNode.addDataran(dataran);

            this.tail.next = dataran;
            dataran.prev = this.tail;
            this.tail = dataran;
            totalDataran += 1;
        }
    }

    static class Dataran {
        CustomInteger height;
        Dataran prev;
        Dataran next;
        boolean isKuil = false;
        // Always stores kuilName, the Dataran's Kuil
        String kuilName;

        public Dataran(CustomInteger value, String kuilName){
            this.height = value;
            this.kuilName = kuilName;
        }

        public Dataran(CustomInteger value, String kuilName, boolean isKuil){
            this.height = value;
            this.kuilName = kuilName;
            this.isKuil = isKuil;
        }
    }

    // Source for original tree 
    // https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/avltree/AVLTree.java
    static class DataranAVLTree {

        // NOTE: 
    
        private Node root;
        CustomInteger counter;
        int tempCount;
    
        public Node find(int key) {
            Node current = root;
            boolean found = false;

            while (current != null) {
                if (current.key == key) {
                    found = true;
                    break;
                }
                current = current.key < key ? current.next : current.prev;
            }
            if(!found) return null;
            return current;
        }
    
        public Node insert(int key) {
            root = insert(root, key);
            if(root.heightValue.value == key)
                return root;
            return find(key);
        }
    
        public void delete(int key, Dataran dataran) {
            root = delete(root, key, dataran, false);
        }
    
        public Node getRoot() {
            return root;
        }
    
        public int height() {
            return root == null ? -1 : root.height;
        }
    
        private Node insert(Node node, int key) {
            if (node == null) {
                return new Node(key);
            } else if (node.key > key) {
                node.prev = insert(node.prev, key);
            } else if (node.key < key) {
                node.next = insert(node.next, key);
            } else {
                return node;
            }
            return rebalance(node);
        }
    
        private Node delete(Node node, int key, Dataran dataran, boolean forced) {
            boolean clear = false;

            if (node == null) {
                return node;
            } else if (node.key > key) {
                node.prev = delete(node.prev, key, dataran, forced);
            } else if (node.key < key) {
                node.next = delete(node.next, key, dataran, forced);
            } else {
                if(node.findDataranNode(dataran) != null || forced){
                    // remove dataran if found
                    if(!forced)
                        node.removeDataran(dataran);
                    if(node.head == null || forced){
                        if (node.prev == null || node.next == null) {
                            node = (node.prev == null) ? node.next : node.prev;
                        } else {
                            Node mostprevChild = mostprevChild(node.next);
                            node.key = mostprevChild.key;
                            node.next = delete(node.next, node.key, dataran, true);
                        }
                        clear = true;
                    }
                }
                        
            }
            if (node != null && clear) {
                node = rebalance(node);
            }
            return node;
        }
    
        private Node mostprevChild(Node node) {
            Node current = node;
            /* loop down to find the prevmost leaf */
            while (current.prev != null) {
                current = current.prev;
            }
            return current;
        }
    
        private Node rebalance(Node z) {
            updateHeight(z);
            int balance = getBalance(z);
            if (balance > 1) {
                if (height(z.next.next) > height(z.next.prev)) {
                    z = rotateprev(z);
                } else {
                    z.next = rotatenext(z.next);
                    z = rotateprev(z);
                }
            } else if (balance < -1) {
                if (height(z.prev.prev) > height(z.prev.next)) {
                    z = rotatenext(z);
                } else {
                    z.prev = rotateprev(z.prev);
                    z = rotatenext(z);
                }
            }
            return z;
        }
    
        private Node rotatenext(Node y) {
            Node x = y.prev;
            Node z = x.next;
            x.next = y;
            y.prev = z;
            updateHeight(y);
            updateHeight(x);
            return x;
        }
    
        private Node rotateprev(Node y) {
            Node x = y.next;
            Node z = x.prev;
            x.prev = y;
            y.next = z;
            updateHeight(y);
            updateHeight(x);
            return x;
        }
    
        private void updateHeight(Node n) {
            n.height = 1 + Math.max(height(n.prev), height(n.next));
        }
    
        private int height(Node n) {
            return n == null ? -1 : n.height;
        }
    
        public int getBalance(Node n) {
            return (n == null) ? 0 : height(n.next) - height(n.prev);
        }
    
        public void inOrderTraversal()  
        {  
            inOrderTraversal(root);  
        }  
        
        public void inOrderTraversal(Node head)  
        {  
            if (head != null)  
            {  
                inOrderTraversal(head.prev);

                // might do iteration inside Node class instead of here
                int count = 0;
                AVLTreeNodeDLL treeDDL = head.head;
                out.print(head.heightValue + "/" + head.totalDataranInNode + "/ ");
                while(treeDDL != null){
                    out.print(treeDDL.getDataran().height + " ");
                    treeDDL = treeDDL.next;
                }

                inOrderTraversal(head.next);
            }  
        }

        public int sweepingTraversal(int limitRight)  
        {  
            counter = new CustomInteger(0);
            sweepingTraversal(root, counter, limitRight); 
            tempCount = counter.value;
            counter = null;
            return tempCount;
        }

        public void sweepingTraversal(Node head, CustomInteger counter, int limitRight)  
        {  
            if (head != null)  
            {  

                sweepingTraversal(head.prev, counter, limitRight);

                // might do iteration inside Node class instead of here
                if(head.heightValue.value - limitRight < 0)
                    counter.value += head.totalDataranInNode;
                    // out.println(head.heightValue.value + "/" + counter);

                if(head.next != null && head.next.heightValue.value - limitRight < 0)
                    sweepingTraversal(head.next, counter, limitRight);
                else if(head.next != null && head.next.prev != null && head.next.prev.heightValue.value - limitRight < 0)
                    sweepingTraversal(head.next.prev, counter, limitRight);
            }  
        }

        public int riseTraversal(int limit, int heightIncrease)  
        {  
            counter = new CustomInteger(0);
            riseTraversal(root, counter, limit, heightIncrease); 
            tempCount = counter.value;
            counter = null;
            return tempCount;
        }

        public void riseTraversal(Node head, CustomInteger counter, int limit, int heightIncrease)  
        {  
            if (head != null)  
            {  

                riseTraversal(head.next, counter, limit, heightIncrease);

                // might do iteration inside Node class instead of here
                if(head.heightValue.value - limit > 0){
                    counter.value += head.totalDataranInNode;
                    head.heightValue.value += heightIncrease;
                    head.key += heightIncrease;
                    // out.println(head.heightValue.value);
                    // out.println(counter.value + "/" + head.totalDataranInNode);
                }
                    // out.println(head.heightValue.value + "/" + counter);

                if(head.prev != null && head.prev.heightValue.value - limit > 0)
                    riseTraversal(head.prev, counter, limit, heightIncrease);
                else if(head.prev != null && head.prev.next != null && head.prev.next.heightValue.value - limit > 0)
                    riseTraversal(head.prev.next, counter, limit, heightIncrease);
            }  
        }

        public int quakeTraversal(int limit, int heightDecrease)  
        {  
            counter = new CustomInteger(0);
            quakeTraversal(root, counter, limit, heightDecrease); 
            tempCount = counter.value;
            counter = null;
            return tempCount;
        }

        public void quakeTraversal(Node head, CustomInteger counter, int limit, int heightDecrease)  
        {  
            if (head != null)  
            {  

                quakeTraversal(head.prev, counter, limit, heightDecrease);

                // FOR QUAKE, PREPARE HERE IF ALL ISLAND FALL TO 1
                if(head.heightValue.value - limit < 0){
                    counter.value += head.totalDataranInNode;
                    head.heightValue.value -= heightDecrease;
                    head.key -= heightDecrease;
                    if(head.key < 1){
                        head.heightValue.value = 1;
                        head.key = 1;
                    }
                }

                if(head.next != null && head.next.heightValue.value - limit < 0)
                    quakeTraversal(head.next, counter, limit, heightDecrease);
                else if(head.next != null && head.next.prev != null && head.next.prev.heightValue.value - limit < 0)
                    quakeTraversal(head.next.prev, counter, limit, heightDecrease);
            }  
        }
    
    
    }

    static class Node {
        // KEY is heightValue and used to iterate AVLTree
        int key;
        // heightVALUE is Dataran height, referenced by Dataran
        CustomInteger heightValue;
        // height is Node height within AVLTree
        int totalDataranInNode;
        int height;
        Node prev;
        Node next;
        AVLTreeNodeDLL head;
        AVLTreeNodeDLL tail;
        AVLTreeNodeDLL temp;

        Node(int key) {
            this.key = key;
            this.heightValue = new CustomInteger(key);
        }

        void increaseDataranHeight(int value){
            this.key += value;
            this.heightValue.increment(value);
        }

        AVLTreeNodeDLL findDataranNode(Dataran target){
            temp = head;
            while(temp != null){
                if(temp.value == target) return temp;
                temp = temp.next;
            }
            return null;
        }

        void addDataran(Dataran dataran){
            if(head == null){
                this.head = new AVLTreeNodeDLL(dataran);
                this.tail = head;
            }
            else {
                this.tail.next = new AVLTreeNodeDLL(dataran);
                this.tail.next.prev = this.tail;
                this.tail = this.tail.next;
            }
            totalDataranInNode += 1;
        }

        void removeDataran(Dataran dataran){
            temp = head;
            while(temp.value != dataran){
                temp = temp.next;
            }

            if(head == tail)
                head = null;
            if(temp == head)
                head = temp.next;
            else if(temp == tail)
                tail = temp.prev;

            if(temp.next != null)
                temp.next.prev = temp.prev;
            if(temp.prev != null)
                temp.prev.next = temp.next;
            
            totalDataranInNode -= 1;
        }
    }

    static class AVLTreeNodeDLL {
        AVLTreeNodeDLL next;
        AVLTreeNodeDLL prev;
        Dataran value;

        public AVLTreeNodeDLL(Dataran value){
            this.value = value;
        }

        public Dataran getDataran(){
            return this.value;
        }
    }

    static class RaidenShogun {
        Kuil currentKuil;
        Dataran currentDataran;

        public RaidenShogun(Kuil kuil, int dataranOrder){
            this.currentKuil = kuil;

            Dataran temp = kuil.head;
            while(dataranOrder != 1){
                dataranOrder--;
                temp = temp.next;
            }

            this.currentDataran = temp;

        }

        public int gerak(String direction, int amount){

            while(amount != 0){
                if(direction.equals("KIRI")){
                    if(currentDataran.prev == null){
                        if(currentKuil.prev == null)
                            return currentDataran.height.value;

                        currentKuil = currentKuil.prev;
                        currentDataran = currentKuil.tail;
                    }
                    else{
                        currentDataran = currentDataran.prev;
                    }
                }
                else{
                    if(currentDataran.next == null){
                        if(currentKuil.next == null)
                            return currentDataran.height.value;

                        currentKuil = currentKuil.next;
                        currentDataran = currentKuil.head;
                    }
                    else{
                        currentDataran = currentDataran.next;
                    }
                }
                amount--;
            }
            
            return currentDataran.height.value;
        }

        public int tebas(String direction, int amount){

            Node tempTreeNode;
            Kuil tempKuil = currentKuil;
            Node treeNode = currentKuil.dataranTree.find(currentDataran.height.value);
            AVLTreeNodeDLL currentDataranNode = treeNode.findDataranNode(currentDataran);

            boolean moved = false;

            while(amount != 0){

                if(direction.equals("KIRI")) {
                    if(currentDataranNode.prev == null){
                        if(tempKuil.prev == null) break;
                        
                        tempKuil = tempKuil.prev;

                        tempTreeNode = tempKuil.dataranTree.find(currentDataran.height.value);
                        if(tempTreeNode == null && tempKuil.prev != null){
                            tempKuil = tempKuil.prev;
                            continue;
                        }
                        if(tempTreeNode != null){
                            treeNode = tempTreeNode;
                            currentDataranNode = treeNode.tail;
                        }
                        
                    }
                    else {
                        currentDataranNode = currentDataranNode.prev;
                    }
                }
                else {
                    if(currentDataranNode.next == null){
                        if(tempKuil.next == null) break;

                        tempKuil = tempKuil.next;

                        tempTreeNode = tempKuil.dataranTree.find(currentDataran.height.value);
                        if(tempTreeNode == null && tempKuil.next != null){
                            tempKuil = tempKuil.next;
                            continue;
                        }
                        if(tempTreeNode != null){
                            treeNode = tempTreeNode;
                            currentDataranNode = treeNode.head;
                        }
                        
                    }
                    else{
                        currentDataranNode = currentDataranNode.next;
                    }
                }

                moved = true;
                amount--;
            }

            if(!moved) return 0;

            currentDataran = currentDataranNode.value;

            if(direction.equals("KIRI")){
                if(currentDataran.next == null){
                    Kuil temp = currentKuil.next;
                    return temp.head.height.value;
                }
                return currentDataran.next.height.value;
            }
            else{
                if(currentDataran.prev == null){
                    Kuil temp = currentKuil.prev;
                    return temp.tail.height.value;
                }
                return currentDataran.prev.height.value;
            }
        }

        public int teleportasi(Kuil target){
            currentKuil = target;
            currentDataran = currentKuil.head;
            return currentDataran.height.value;
        }

        public void getCurrentStatus(){
            out.println(currentKuil.nama + " " + currentDataran.height + " " + currentDataran.isKuil);
        }
    }

}
