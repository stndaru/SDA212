package UTS;
/* Tuliskan Nama, NPM, dan Kelasmu di sini:
** Nama Lengkap: Stefanus Ndaru Wedhatama
** NPM         : 2006526812
** Kelas       : SDA D
*/


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;
import java.math.*;

/**
 *
 * @author solic
 */
class DLinkedList{
    DNode header = new DNode();
    DNode mid;
    DNode last;
    int size = 0;
    int midPos = 0;
    
    /*Method TO DO*/
    void insertLast(char data){
        // base case first element
        if(header.next == null){
            header.next = new DNode(data);
            last = header.next;
            mid = header.next;
        }

        else{
            last.next = new DNode(data, last, null);
            last = last.next;
        }

        size += 1;

        if(midPos < Math.ceil(size/2)){
            while(midPos != Math.ceil(size/2)){
                mid = mid.next;
                midPos += 1;
            }
        }

        else if(midPos > Math.ceil(size/2)){
            while(midPos != Math.ceil(size/2)){
                mid = mid.prev;
                midPos -= 1;
            }
        }

        if(size % 2 == 0){
            mid = mid.prev;
            midPos -= 1;
        }
    }
    
    void loadDataToList(char[] inputList){
        for(char i : inputList){
            insertLast(i);
        }
    }
    
    boolean isEmpty(){
        return header.next == null;
    }
    
    @Override
    public String toString(){
        if(header.next != null)
            return header.next.toString();
        return "-";
    }
}

public class Soal01 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
		
        DLinkedList theList = new DLinkedList();
        
        char[] input = sc.next().toCharArray();
        
        theList.loadDataToList(input);
        // System.out.println(theList); //cetak theList jika ingin mengecek isi linked list-nya
        specificPrint(theList);
    }
    
    /*given method*/
    static void specificPrint(DLinkedList theList){
        
        /*cetak dari mid ke awal*/
        MyIterator midItr = new MyIterator(theList);
        midItr.toMid();
        while(midItr.isInList()){
            System.out.print(midItr.getData());
            midItr.retreat();
        }
        
        /*cetak dari last ke sebelum mid*/
        MyIterator backItr = new MyIterator(theList);
        backItr.toLast();        
        while(backItr.isInList() && backItr.current != theList.mid){
            System.out.print(backItr.getData());
            backItr.retreat();
        }
    }
}

class DNode{
    char data;
    DNode prev;
    DNode next;
    
    DNode(){}
    
    DNode(char data){this.data = data;}
    
    DNode(char data, DNode prev, DNode next){
        this.data = data;
        this.prev = prev;
        this.next = next;
    }
    
    @Override
    public String toString(){
        String result = "" + data;
        if(next != null)
            result += next.toString();
        return result;
    }
}

class MyIterator{
    DLinkedList theList;
    DNode current;
    
    MyIterator(DLinkedList theList){
        this.theList = theList;
        toFirst();
    }
    
    boolean isInList(){
        return current != null && current != theList.header;
    }
    
    char getData(){
        if(isInList())
            return current.data;
        return '-';
    }
    
    void toFirst(){
        if(!theList.isEmpty())
            current = theList.header.next;
    }
    
    void toLast(){
        if(!theList.isEmpty())
            current = theList.last;
    }
    
    void toMid(){
        if(!theList.isEmpty())
            current = theList.mid;
    }
    
    void advance(){
        current = current.next;
    }
    
    void retreat(){
        current = current.prev;
    }
}