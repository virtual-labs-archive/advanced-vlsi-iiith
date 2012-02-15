<div id='Container'>
<br/>
<br/>
<p class="tag_title">
<div class='tab_heading' > Introduction
</div>	

<br/>
</p>
<center>
<div class='def_cont'>
<h3>Error-correcting Code:</h3>
<div>
In complex memory circuits, errors might arise while storing or retrieving binary data. To improve the reliability of such circuits, error-detecting and correcting codes are deployed. The most widely used error-detection scheme is the parity bit. A parity bit is generated and stored along with the data word in the memory. The parity of the word is verified after reading it from the memory. If the parity sense is correct, the data word is accepted. If the parity checked results in an inversion, an error is detected, but it cannot be rectified.
<br/>
<br/>


n error-correcting code generates check bits that are stored with the data word in memory. Each check bit gives the parity over a group of bits in the data word. When the data is read from the memory, the check bits are also read along with the data bits and compared with a new set of check bits generated from the read data. If the check bits match, it implies that no error has occurred. If  the check bits do not match with the original parity bits, they generate a unique pattern, called a syndrome, that can be used to identify the erroneous bit. A single error occurs when a bit flips from 0 to 1 or from 1 to 0 during the write or read operation. If the erroneous bit is identified, then the error can be corrected by flipping the erroneous bit.

<br/>
<br/>
<h3>Hamming Code:</h3>
<br />
Richard Wesley Hamming was an American mathematician. He invented an error-correcting code in 1950, while working at the Bell Telephone Laboratories. It is the most widely used error-correcting code in random-access memories. The code is named after him and known as Hamming code. The Hamming code can detect up to 2 errors but correct only one error. Usually, the probability of an error is low and that of a double error in the same word, is lower still. Hence we are content with the capability to rectify a single error.
<br />
</div>

</div>

</div>
</div>


<br />
</center>
</div>
