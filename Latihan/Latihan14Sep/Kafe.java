// nama package dihapus apabila menyebabkan masalah
package Latihan.Latihan14Sep;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Kafe {

    private static InputReader in;
    private static PrintWriter out;
    private static Scanner inp = new Scanner(System.in);

    public static void main(String[] args) {
        // Asumsi menerima input, nama hanya 1 kata, npm unik

        PriorityQueue<Mahasiswa> mhs = new PriorityQueue<>();

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        
        System.out.println("Hardcode Input or Custom Input (Y/N)? ");
        String ask = inp.nextLine().toLowerCase();

        if(ask.equals("y")){
            System.out.println("Masukkan Jumlah Perintah: ");
            int N;
            N = in.nextInt();

            System.out.println("Format Input:");
            System.out.println("<nama 1 kata> <ipk> <npm>");
            System.out.println("\nDi akhir akan diberikan queuenya\n");
            
            for(int tmp=0;tmp<N;tmp++) {

                String nama = in.next();
                double ipk = Double.parseDouble(in.next());
                int npm = in.nextInt();

                mhs.add(new Mahasiswa(nama, ipk, npm));
                
                
            }
        }

        else if(ask.equals("n")){
            mhs.add(new Mahasiswa("Andi", 2.0, 123456));
            mhs.add(new Mahasiswa("Dika", 3.0, 123452));
            mhs.add(new Mahasiswa("Luwa", 4.0, 123457));
            mhs.add(new Mahasiswa("Pega", 3.2, 123453));
            mhs.add(new Mahasiswa("Beda", 3.0, 123459));
            mhs.add(new Mahasiswa("Pega", 3.2, 123451));
        }

        System.out.println("\nHasil Queue:");
        while(!mhs.isEmpty()){
            Mahasiswa temp = mhs.poll();
            System.out.println(temp.nama + " " + temp.IPK + " " + temp.NPM);
        }
        

        out.close();
    }

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
}


