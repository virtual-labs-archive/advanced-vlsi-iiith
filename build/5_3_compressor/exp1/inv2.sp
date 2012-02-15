*inverter
.include '45nm_HP.pm'
cl out 0 100f
M1 out in Vdd Vdd PMOS l=50n w= 450n
M2 out in 0 0  NMOS l=50n w=100n
Vdd Vdd 0 1.1
Vin in 0 pulse (0 1.1 0 1n 1n 10n 22n)
.tran 1n 100n 
*.print tran V(in)
.save  V(in) V(out)
.end

