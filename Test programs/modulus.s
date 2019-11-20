		load 	0xf0f0, %ax  
		load 	0xf0f1, %bx	
		cmp 	%ax, %bx
		jl 		end
		cmp 	$0, %bx
		je 		end0
		cmp		$1, %bx
		je 		end0

		mov 	%bx, %cx
		

loop:	sub		%bx, %ax
		cmp		%cx, %ax
		jl		loop

end:	store	%ax, 0xf0f2
		hlt

end0:	mov 	$0, %ax
		store	%ax, 0xf0f2
		hlt


