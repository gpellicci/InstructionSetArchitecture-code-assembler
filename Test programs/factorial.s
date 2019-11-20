		load 	0xf0f0, %ax
		cmp 	$0, %ax
		je 		end1
		cmp 	$1, %ax
		je 		end1
		mov 	$1, %dx
		mov 	$1, %cx
loop:	inc 	%cx
		mul 	%cx, %dx		
		cmp 	%ax, %cx
		jne 	loop
		store 	%dx, 0xf0f2	
		hlt
end1:	mov 	$1, %ax
		store 	%ax, 0xf0f2
		hlt