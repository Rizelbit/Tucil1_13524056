# 👑 Queens LinkedIn Solver

> **Tugas Kecil 1 IF2211 Strategi Algoritma** > Penyelesaian Permainan Queens Menggunakan Algoritma Brute Force Murni

---

## Deskripsi Singkat
**Queens LinkedIn Solver** adalah aplikasi antarmuka grafis (GUI) berbasis JavaFX yang dirancang untuk menyelesaikan permainan logika "Queens" dari LinkedIn. Program ini mengimplementasikan algoritma **Brute Force** untuk mengeksplorasi ruang pencarian dan menemukan susunan Ratu di atas papan yang mematuhi semua aturan berikut:
1. Hanya boleh ada tepat satu Ratu di setiap baris.
2. Hanya boleh ada tepat satu Ratu di setiap kolom.
3. Hanya boleh ada tepat satu Ratu di setiap wilayah blok warna.
4. Antar Ratu tidak boleh saling bersentuhan, baik secara vertikal, horizontal, maupun diagonal.

Program ini dilengkapi dengan fitur *visualizer* interaktif, peringatan *error handling* untuk peta yang tidak valid, serta fitur ekspor solusi ke dalam file teks (`.txt`) maupun gambar (`.png`).

---

## Kebutuhan Sistem
Untuk dapat melakukan kompilasi dan menjalankan program ini, pastikan komputer Anda sudah terpasang perangkat lunak berikut:
* **Java Development Kit (JDK) 17** atau versi yang lebih baru. Cara instalasi:
  ### Linux
  ```bash
  sudo apt install openjdk-17-jdk
  ```
  ### Windows
  Unduh Java 17 ke atas pada link berikut. https://www.oracle.com/id/java/technologies/downloads/

---

## Cara Kompilasi dan Menjalankan Program (CLI)
Program ini dirancang agar sangat mudah dijalankan melalui Terminal atau *Command Line Interface* (CLI) tanpa memerlukan instalasi IDE khusus.

Ikuti langkah-langkah berikut:

1. Buka Terminal (Linux/Mac) atau Command Prompt / PowerShell (Windows).
2. Lakukan *clone* repositori ini ke dalam komputer Anda dengan menjalankan perintah:\
    ```bash
    git clone https://github.com/Rizelbit/Tucil1_13524056.git
    ```
3. Ubah direktori kerja Anda ke dalam folder utama repositori ini:
    ```bash
   cd Tucil1_13524056
   ```
4. Jalankan perintah kompilasi dan eksekusi menggunakan Maven Wrapper:
   * Untuk Pengguna Windows: 
   ```bash
   .\mvnw clean javafx:run
   ```
   * Untuk Pengguna Linux:
   ```bash
   chmod +x mvnw
   ./mvnw clean javafx:run
   ```
5. Tunggu beberapa saat hingga Maven mengunduh pustaka JavaFX (jika belum ada) dan menyelesaikan proses kompilasi. Setelah selesai, jendela GUI aplikasi akan terbuka secara otomatis!

## Cara Penggunaan Aplikasi
1. Setelah jendela program terbuka, klik tombol **Pilih File Input**
2. Jendela penjelajah file akan mengarahkan Anda ke folder `test/`. Pilih salah satu file konfigurasi papan berformat `.txt`.
3. Program akan memproses dan secara otomatis mencari solusi secara *multithreading*.
4. Jika solusi ditemukan, program akan merender papan warna-warni lengkap dengan ikon Mahkota Ratu (♛) di posisi yang tepat, beserta informasi **Waktu Pencarian (ms)** dan **Banyak Kasus Ditinjau**.
5. Jika peta tidak memiliki solusi, program akan menampilkan peringatan merah dan hanya menampilkan papan kosong.
6. Simpan hasil solusi Anda menggunakan tombol yang disediakan:
    * **Simpan Teks**: Menyimpan posisi matriks solusi dan log performa dalam format `.txt`.
    * **Simpan Gambar**: Menyimpan tangkapan layar papan cantik tersebut dalam format `.png`.

## Format File Input (.txt)
Program menerima input file teks berisikan representasi matriks persegi berukuran $N \times N$. Setiap huruf alfabet (A-Z) merepresentasikan wilayah warna yang berbeda. Huruf tidak boleh dipisahkan oleh spasi.
```text
AABBB
ACCBD
ACEED
AEEED
AADDD
```

## Author
|   NIM    |            Nama            |
|:--------:|:--------------------------:|
| 13524056 | Reinhard Alfonzo Hutabarat |