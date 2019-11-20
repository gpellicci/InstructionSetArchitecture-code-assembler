		mov $10, %ax
		store %ax, 0xf0f0
		
		load 0xf0f0, %dx
		cmp $0, %dx
		je end0
		cmp $1, %dx
		je end1

		mov $2, %cx
		mov $0, %ax
		mov $1, %bx
		
loop:	xchg %ax, %bx
		add %ax, %bx
		inc %cx
		cmp %cx, %dx
		je end

end0:	mov	$0, %ax
		store %ax, 0xf0f2
		hlt

end1:	mov $1, %ax
		store %ax, 0xf0f2
		hlt

end:	store %bx, 0xf0f2
		hlt