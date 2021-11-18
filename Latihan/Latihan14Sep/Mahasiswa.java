// nama package dihapus apabila menyebabkan masalah
package Latihan.Latihan14Sep;

public class Mahasiswa implements Comparable<Mahasiswa> {
    String nama;
    Double IPK;
    Integer NPM;

    public Mahasiswa(String nama, double ipk, int npm){
        this.nama = nama;
        this.IPK = ipk;
        this.NPM = npm;
    }

    @Override
    public int compareTo(Mahasiswa o){
        if(this.IPK.compareTo(o.IPK) != 0)
            return -1*this.IPK.compareTo(o.IPK);
        else if(this.nama.toLowerCase().compareTo(o.nama.toLowerCase()) != 0)
            return this.nama.compareTo(o.nama);
        return this.NPM.compareTo(o.NPM);

    }
}