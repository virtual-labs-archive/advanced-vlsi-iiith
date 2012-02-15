<div id='Container'>
<br/>
<br/>
<p class="tag_title">
<div class='tab_heading' > Procedure 
</div>	

<br/>

 <p ><font size ="3">
In circuitary theory, NOT, AND and OR gates are the basic gates. Any circuit can be designed
using these gates. The symbols NOT gate, AND gate, and OR gate are also considered as basic
basic circuit symbols, which are used to build general circuits. We can implement any logical circuit
using  these logic gates.
</p><br><br>
<b>Here is an example of Full adder circuit using logic gates:</b><br><br>
<br />
<h2>Full Adder</h2>
 <p><font size="3">
 A full adder adds binary numbers and accounts for values carried in as well as out.
 A one-bit full adder adds three one-bit numbers, often written as A, B, and Cin;
 A and B are the operands, and Cin is a bit carried in (in theory from a past addition).
 The circuit produces a two-bit output sum typically represented by the signals Cout and S,
 where <b>sum= 2* Cout + S.</b> The one-bit full adder's truth table is:
  </p><br><br>
  <table border="1" align ="center">
       <thead>
                                                 <tr>
                                                     <td colspan="3">Inputs</td>
                                                     <td colspan="2">Outputs</td>
                                                    
                                                 </tr>
                                             </thead>
                                             <tbody>
                                                 <tr>

                                                     <th>A</th>
                                                     <th>B</th>
                                                     <th>Cin</th>
                                                     <th>Cout</th>
                                                     <th>S</th>
                                                 </tr>
                                                 <tr>
                                                     <td>0</td>
                                                     <td>0</td>
                                                     <td>0</td>
                                                     <td>0</td>
                                                     <td>0</td>
                                                 </tr>
                                                 <tr>
                                                     <td>1</td>
                                                     <td>0</td>
                                                     <td>0</td>
                                                     <td>0</td>
                                                     <td>1</td>
                                                 </tr>
                                                 <tr>
                                                     <td>0</td>
                                                     <td>1</td>
                                                     <td>0</td>
                                                     <td>0</td>
                                                     <td>1</td>
                                                 </tr>
                                                 <tr>
                                                     <td>1</td>
                                                     <td>1</td>
                                                     <td>0</td>
                                                     <td>1</td>
                                                     <td>0</td>
                                                 </tr>
                                                 <tr>
                                                     <td>0</td>
                                                     <td>0</td>
                                                     <td>1</td>
                                                     <td>0</td>
                                                     <td>1</td>
                                                 </tr>
                                                 <tr>
                                                     <td>1</td>
                                                     <td>0</td>
                                                     <td>1</td>
                                                     <td>1</td>
                                                     <td>0</td>
                                                 </tr>
                                                 <tr>
                                                     <td>0</td>
                                                     <td>1</td>
                                                     <td>1</td>
                                                     <td>1</td>
                                                     <td>0</td>
                                                 </tr>
                                                 <tr>
                                                     <td>1</td>
                                                     <td>1</td>
                                                     <td>1</td>
                                                     <td>1</td>
                                                     <td>1</td>
                                                 </tr>

                                             </tbody>
                                         </table>
          <p><br><br>
            A full adder can be implemented in many different ways such as with a custom transistor-level
            circuit or composed of other gates. 
          </p>


<p>
 A full adder can be constructed from two half adders by connecting A and B to the input
 of one half adder, connecting the sum from that to an input to the second adder,
 connecting Ci to the other input and OR the two carry outputs. Equivalently, S could be
 made the three-bit XOR of A, B, and Ci, and Co could be made the three-bit majority function
 of A, B, and Ci.
 </p>
 <br>
 <img src="fpga/220px-Full_Adder.svg.jpg" width="300" height="200" >
 <br>

<h2>Procedure to design Full adder:-</h2>
<br><br>
<b>(1)</b> Select three Input and one output button from tool bar A,B, Cin and Cout.<br>
<b>(2)</b> Select two XOR , two AND and one OR gate from tool bar also.<br>
<b>(3)</b> Connect all of them through wire as shown in the picture and finally the output of the the circuit will be connected
           through the output &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;button.<br>
<b>(4)</b> Press simulation button to test the circuit.<br>
<b>(5)</b> If connection will be correct then enter input through input port and press simulation button and see the output<br>
<b>(6)</b> Two output button will be show you the result of Sum and Carry bit.
<br>
<div class='def_cont'>
</div>



</div>
