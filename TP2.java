import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class TP2 {
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
        So many TLE and RTE. The TLE part is inevitable
    */

    
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
            Kuil tempKuil = null;
            Pulau tempPulau = null;
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
                    for(Pulau j : pulauMap.values()){
                        tempKuil = j.kuilMap.get(pulauBaru);
                        if(tempKuil != null){
                            tempPulau = j;
                            break;
                        }
                    }
                    out.println(Pisah(tempKuil, tempPulau));
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
                    for(Pulau j : pulauMap.values()){
                        tempKuil = j.kuilMap.get(direction);
                        if(tempKuil != null) break;
                    }
                    out.println(Teleportasi(tempKuil));
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
                    Sanity(4);
                    break;

                case "STATUS":
                    raidenShogun.getCurrentStatus();
                    break;

                default:
                    break;
            }
        }

        // String random = in.next();
        out.flush();
    }

    static int Unifikasi(Pulau depan, Pulau belakang){
        int totalDataran = 0;

        depan.tail.next = belakang.head;
        belakang.head.prev = depan.tail;
        depan.tail = belakang.tail;

        for(String key : belakang.kuilMap.keySet()){
            depan.kuilMap.put(key, belakang.kuilMap.get(key));
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

        // out.println("STABILIZING");

        targetDataran = (currentDataran.prev.height.value >= currentDataran.height.value) ? currentDataran : currentDataran.prev;
        Dataran newDataran = new Dataran(targetDataran.height, targetDataran.kuilName);
        
        if(currentDataran.next != null){
            tempDataran = currentDataran.next;
            currentDataran.next = newDataran;
            tempDataran.prev = newDataran;
            newDataran.next = tempDataran;
            newDataran.prev = currentDataran;

            Node treeNode = currentKuil.dataranTree.find(targetDataran.height.value);
            AVLTreeNodeDLL treeNodeDLL = treeNode.findDataranNode(targetDataran);

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
                //out.println("NEXT/"+treeNodeDLL.next.value.height.value);//
            }
            treeNode.totalDataranInNode += 1;

        }
        else {
            currentDataran.next = newDataran;
            currentDataran.next.prev = currentDataran;
            currentKuil.tail = newDataran;

            Node treeNode = currentKuil.dataranTree.find(targetDataran.height.value);
            if(treeNode == null) return 0;
            treeNode.tail.next = new AVLTreeNodeDLL(newDataran);
            treeNode.tail.next.prev = treeNode.tail;
            treeNode.tail = treeNode.tail.next;
            treeNode.totalDataranInNode += 1;
        }

        currentKuil.totalDataran += 1;

        return targetDataran.height.value;
    }

    static int Sweeping(Pulau pulau, int heightRisen){
        return pulau.sweeping(heightRisen);
    }

    static void Sanity(int val){
        out.println();
        sanityCheck(val);
        raidenShogun.getCurrentStatus();
        out.println("================");
        out.println();
    }

    static void sanityCheck(int val){
        // out.println(pulauMap);
        // out.println(kuilMapGeneral);
        for(Pulau i : pulauMap.values()){
            out.println("\n== GANTI PULAU ==");
            Kuil temp = i.head;
            while(temp != null){
                out.print(temp.nama + " ");
                temp.sanityCheckKuil();
                out.println(temp.dataranTree.countLesser(val));
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
                count += temp.dataranTree.countLesser(heightRisen);
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
            CustomInteger existZero = new CustomInteger(0);
            temp = head;
            while(temp != null){
                count += temp.dataranTree.quakeTraversal(height, heightRisen, existZero);

                // TLE fest right here bois
                // Basically if detects multiple Dataran at value 1
                // will go O(n) throughout EVERY dataran and find all valued 1
                if(existZero.value == 1){
                    Dataran tempDataran;
                    Node oneNode = null;
                    tempDataran = temp.head;
                    while(tempDataran != null){
                        if(tempDataran.height.value == 1){
                            if(oneNode == null)
                                oneNode = temp.dataranTree.insert(1);
                            oneNode.addDataran(tempDataran);
                        }
                        tempDataran = tempDataran.next;
                    }
                }

                existZero.value = 0;
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
            
            if(treeNode.head != null){
                // dataranTree.findInsert(heightVal);
            } 
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
                // out.println(current.key+"/"); //
            }
            if(!found) return null;
            return current;
        }

        public Node findInsert(int key) {
            Node current = root;
            boolean found = false;

            while (current != null) {
                if (current.key == key) {
                    // current.desc++;
                    found = true;
                    break;
                }
                else if(current.key > key) {
                    current.desc++;
                    current = current.prev;
                }
                else if(current.key < key) {
                    current.desc++;
                    current = current.next;
                }
                // current = current.key < key ? current.next : current.prev;
                // out.println(current.key+"/"); //
            }
            if(!found) return null;
            return current;
        }
    
        public Node insert(int key) {
            root = insert(root, key);

            if(root.heightValue.value == key)
                return root;

            Node foundNode = find(key);
            // findInsert(key);

            return foundNode;
        }
    
        public void delete(int key, Dataran dataran) {
            CustomInteger countZero = new CustomInteger(0);
            root = delete(root, key, dataran, false, countZero, false);
        }
    
        public int height() {
            return root == null ? -1 : root.height;
        }
    
        private Node insert(Node node, int key) {
            if (node == null) {
                return new Node(key);
            } else if (node.key > key) {
                node.prev = insert(node.prev, key);
                node.desc++;
            } else if (node.key < key) {
                node.next = insert(node.next, key);
                node.desc++;
            } else {
                node.desc++;
                return node;
            }
            return rebalance(node);
        }
    
        private Node delete(Node node, int key, Dataran dataran, boolean forced, CustomInteger countZero, boolean purge) {
            boolean clear = false;
            boolean isRoot = false;

            if (node == null) {
                return node;
            } else if (node.key > key) {
                node.prev = delete(node.prev, key, dataran, forced, countZero, purge);
                node.desc--;
            } else if (node.key < key) {
                node.next = delete(node.next, key, dataran, forced, countZero, purge);
                node.desc--;
            } else {
                if(node.findDataranNode(dataran) != null || forced){
                    // remove dataran if found
                    if(purge){
                        node.head = null;
                        node.tail = null;
                        node.totalDataranInNode = 0;
                    }
                    else if(!forced){
                        node.removeDataran(dataran);
                    }

                    // out.println(node+"/"+node.key+"/"+node.head);
                        
                    if(node.head == null || node.totalDataranInNode <= 0 || forced){
                        // out.println(node.key + "/");
                        if (node.prev == null || node.next == null) {
                            node = (node.prev == null) ? node.next : node.prev;
                        } else {
                            Node mostprevChild = mostprevChild(node.next);
                            node.key = mostprevChild.key;
                            node.heightValue = mostprevChild.heightValue;
                            node.totalDataranInNode = mostprevChild.totalDataranInNode;
                            node.head = mostprevChild.head;
                            node.tail = mostprevChild.tail;
                            node.next = delete(node.next, node.key, dataran, true, countZero, false);
                            node.desc--;
                        }
                        clear = true;
                    }
                }
                        
            }

            if (node != null && clear) {
                // out.println(node.key+"/");
                if(node == root && node.next == null && node.prev.next == null && node.prev.prev == null){
                    // out.println("ROOOT HERE"+node.key);
                    isRoot = true;
                    return root;
                } 
                node = rebalance(node);
            }

            if(isRoot) return root;

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
            Node x = null;
            Node z = null;
            x = y.prev;
            if(x!=null){
                z = x.next;
                x.next = y;
                y.prev= z;
                updateHeight(x);
                int val = (z != null) ? z.desc : -1;
                y.desc = y.desc - (x.desc + 1) + (val + 1);
                x.desc = x.desc - (val + 1) + (y.desc + 1);
            }
            
            updateHeight(y);
            return x;

            // Node x = y.prev;
            // Node z = x.next;
            // x.next = y;
            // y.prev = z;
            // updateHeight(y);
            // updateHeight(x);
            // return x;
        }
    
        private Node rotateprev(Node y) {
            Node x = null;
            Node z = null;
            x = y.next;
            if(x!=null){
                z = x.prev;
                x.prev = y;
                y.next = z;
                updateHeight(x);
                int val = (z != null) ? z.desc : -1;
                y.desc = y.desc - (x.desc + 1) + (val + 1);
                x.desc = x.desc - (val + 1) + (y.desc + 1);
            }

            updateHeight(y);
            return x;

            // Node x = y.next;
            // Node z = x.prev;
            // x.prev = y;
            // y.next = z;
            // updateHeight(y);
            // updateHeight(x);
            // return x;
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
            if(root == null) out.print("TREE IS EMPTY");
            inOrderTraversal(root);  
        }  
        
        public void inOrderTraversal(Node head)  
        {  
            if (head != null)  
            {  
                inOrderTraversal(head.prev);

                // might do iteration inside Node class instead of here
                AVLTreeNodeDLL treeDDL = head.head;
                out.print(head.heightValue + "/" + head.totalDataranInNode + "/" + head.desc + "// ");
                while(treeDDL != null){
                    out.print(treeDDL.getDataran().height + " ");
                    treeDDL = treeDDL.next;
                }

                inOrderTraversal(head.next);
            }  
        }


        public int riseTraversal(int limit, int heightIncrease)  
        {  
            counter = new CustomInteger(0);
            // out.println(root.key);
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
                }
                    // out.println(head.heightValue.value + "/" + counter);

                if(head.prev != null && head.prev.heightValue.value - limit > 0)
                    riseTraversal(head.prev, counter, limit, heightIncrease);
                else if(head.prev != null && head.prev.next != null)
                    riseTraversal(head.prev.next, counter, limit, heightIncrease);
            }  
        }

        public int quakeTraversal(int limit, int heightDecrease, CustomInteger existZero)  
        {  
            counter = new CustomInteger(0);
            CustomInteger countZero =  new CustomInteger(0);
            quakeTraversal(root, counter, limit, heightDecrease, countZero); 
            tempCount = counter.value;
            counter = null;
            if(countZero.value > 1){
                Node zero = find(1);
                existZero.value = 1;
                while(zero != null){
                    root = delete(root, 1, zero.head.value, false, countZero, true);
                    zero = find(1);
                }
            }
            countZero = null;
            return tempCount;
        }

        public void quakeTraversal(Node head, CustomInteger counter, int limit, int heightDecrease, CustomInteger countZero)  
        {  
            if (head != null)  
            {  

                quakeTraversal(head.prev, counter, limit, heightDecrease,countZero);

                // FOR QUAKE, PREPARE HERE IF ALL ISLAND FALL TO 1
                if(head.heightValue.value - limit < 0){
                    counter.value += head.totalDataranInNode;
                    head.heightValue.value -= heightDecrease;
                    head.key -= heightDecrease;
                    if(head.key < 1){
                        head.heightValue.value = 1;
                        head.key = 1;
                    }
                    if(head.key == 1){
                        countZero.value++;
                    }
                }

                if(head.next != null && head.next.heightValue.value - limit < 0)
                    quakeTraversal(head.next, counter, limit, heightDecrease, countZero);
                else if(head.next != null && head.next.prev != null && head.next.prev.heightValue.value - limit < 0)
                    quakeTraversal(head.next.prev, counter, limit, heightDecrease, countZero);
            }  
        }
    
        public int countLesser(int x){
            int res = 0;
            Node head = root;
    
            while(head != null){
                int desc = (head.prev != null) ? head.prev.desc : -1;
    
                if(head.key < x){
                    res = res + desc + 1 + head.totalDataranInNode;
                    head = head.next;
                }
                else if(head.key > x)
                    head = head.prev;
                else{
                    res = res + desc + 1;
                    break;
                }
            }
            return res;
        }
    
        public Node floor(Node root, int key)
        {
            if (root == null)
                return null;
    
            /* If root->data is equal to key */
            if (root.key == key)
                return root;
    
            /* If root->data is greater than the key */
            if (root.key > key)
                return floor(root.prev, key);
    
            /* Else, the floor may lie in right subtree
            or may be equal to the root*/
            Node floorValue = floor(root.next, key);
            if(floorValue == null) return root;

            return (floorValue.key <= key) ? floorValue : root;
        }
    
    }

    // https://www.geeksforgeeks.org/count-greater-nodes-in-avl-tree/
    static class Node {
        // KEY is heightValue and used to iterate AVLTree
        int key;
        // heightVALUE is Dataran height, referenced by Dataran
        CustomInteger heightValue;
        // height is Node height within AVLTree
        int totalDataranInNode;
        int height;
        int desc = 0;
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

        void sanityNode(Dataran target){
            out.println(target+"/"+this.totalDataranInNode+"/"+this.heightValue.value);
            temp = head;
            while(temp != null){
                out.println(temp+"//"+temp.value);
                temp = temp.next;
            }
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
            else if(temp == head)
                head = temp.next;
            else if(temp == tail){
                tail = temp.prev;
                tail.next = null;
                totalDataranInNode -= 1;
                return;
            }
                

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
            Kuil markedKuil = currentKuil;
            Node treeNode = currentKuil.dataranTree.find(currentDataran.height.value);
            AVLTreeNodeDLL currentDataranNode = treeNode.findDataranNode(currentDataran);

            boolean moved = false;

            while(amount != 0){

                if(direction.equals("KIRI")) {
                    if(currentDataranNode.prev == null){
                        // logic error here
                        if(tempKuil.prev == null) break;
                        
                        // out.println(tempKuil.nama + "/" + tempKuil.prev.nama);
                        tempKuil = tempKuil.prev;

                        tempTreeNode = tempKuil.dataranTree.find(currentDataran.height.value);
                        if(tempTreeNode == null && tempKuil.prev != null){
                            continue;
                        }
                        if(tempTreeNode != null){
                            treeNode = tempTreeNode;
                            currentDataranNode = treeNode.tail;
                            markedKuil = tempKuil;
                            amount--;
                            moved = true;
                        }
                        
                    }
                    else {
                        currentDataranNode = currentDataranNode.prev;
                        amount--;
                        moved = true;
                    }
                }
                else {
                    if(currentDataranNode.next == null){
                        if(tempKuil.next == null) break;

                        tempKuil = tempKuil.next;

                        tempTreeNode = tempKuil.dataranTree.find(currentDataran.height.value);
                        if(tempTreeNode == null && tempKuil.next != null){
                            continue;
                        }
                        if(tempTreeNode != null){
                            treeNode = tempTreeNode;
                            currentDataranNode = treeNode.head;
                            markedKuil = tempKuil;
                            amount--;
                            moved = true;
                        }
                        
                    }
                    else{
                        currentDataranNode = currentDataranNode.next;
                        amount--;
                        moved = true;
                    }
                }
            }

            // out.println(amount + "/");
            if(!moved) return 0;

            currentDataran = currentDataranNode.value;
            currentKuil = markedKuil;

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
            int prev = 0;
            int next = 0;
            if(currentDataran.prev != null) prev = currentDataran.prev.height.value;
            if(currentDataran.next != null) next = currentDataran.next.height.value;
            out.println(currentKuil.nama + " " + prev + "/" + currentDataran.height + "/" + next + " " + currentDataran.isKuil);
        }
    }

}
