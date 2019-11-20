		load 	0xf0f0, %ax  
		load 	0xf0f1, %bx	
		cmp 	$0, %bx
		je 		end0
		mov 	$1, %dx
		mov 	$0, %cx

loop:	mul		%ax, %dx
		inc 	%cx
		cmp 	%cx, %bx
		jne		loop
		
end:	store	%dx, 0xf0f2
		hlt

end0:	mov		$0, %ax
		store	%ax, 0xf0f2
		hlt