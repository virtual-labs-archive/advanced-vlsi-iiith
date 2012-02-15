<div id='Container'>
<br/>
<br/>
<p class="tag_title">
<div class='tab_heading' > Theory</div>

It is possible to create a logical circuit using multiple full adders to add N-bit numbers. Each full adder inputs a Cin, which is the Cout of the previous adder. This kind of adder is a ripple carry adder, since each carry bit "ripples" to the next full adder. Note that the first (and only the first) full adder may be replaced by a half adder. The layout of a ripple carry adder is simple, which allows for fast design time; however, the ripple carry adder is relatively slow, since each full adder must wait for the carry bit to be calculated from the previous full adder. The gate delay can easily be calculated by inspection of the full adder circuit. Each full adder requires three levels of logic. In a 32-bit [ripple carry] adder, there are 32 full adders, so the critical path (worst case) delay is 31 * 2(for carry propagation) + 3(for sum) = 65 gate delays. 
<h3>Delay</h3>
We know that combinational logic circuits can't compute the outputs instantaneously. There is some delay between the time the inputs are sent to the circuit, and the time the output is computed.

Let's say the delay is T units of time.

Suppose you want to implement an n-bit ripple carry adder. How much total delay is there?

Since an n-bit ripple carry adder consists of n adders, there will be a delay of nT. This is O(n) delay.

Why is there this much delay? After all, aren't the adders working in parallel?

While the adders are working in parallel, the carrys must "ripple" their way from the least significant bit and work their way to the most significant bit. It takes T units for the carry out of the rightmost column to make it as input to the adder in the next to rightmost column.

Thus, the carries slow down the circuit, making the addition linear with the number of bits in the adder.

This is not a big problem, usually, because hardware adders are fixed in size. They add, say, 32 bits at a time. There's no way to make an adder add an arbitrary number of bits. It can be done in software, not in hardware. In effect, this is what makes hardware "hard". It's not flexible to change.

Even though there are a fixed number of bits to add in an adder, there are ways to make the adder add more quickly (at least, by a constant).

Carry lookahead adders add much faster than ripple carry adders. They do so by making some observations about carries. We will look at this at a later set of notes. 

<div>
</div>



