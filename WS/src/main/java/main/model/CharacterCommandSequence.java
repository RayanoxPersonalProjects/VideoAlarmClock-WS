package main.model;

/**
 * Define the character sequence to get for each character needed, from the reset position (right - up) of the keyboard. 
 * 
 * @author rbenhmidane
 *
 */
@Deprecated()
public enum CharacterCommandSequence {
	
	//TODO (At home): To implement for each character
	
	a (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	b (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	c (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	d (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	e (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	f (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	g (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	h (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	i (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	j (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	k (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	l (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	m (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	n (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	o (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	p (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	q (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	r (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	s (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	t (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	u (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	v (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	w (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	x (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	y (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down}),
	z (new PsButton [] { PsButton.Left, PsButton.Left,PsButton.Left, PsButton.Down, PsButton.Down});
	
	private PsButton [] btnSequence;
	
	private CharacterCommandSequence(PsButton... btnSequence) {
		this.btnSequence = btnSequence;
	}
	
	public PsButton[] getBtnSequence() {
		return btnSequence;
	}
	
	public static CharacterCommandSequence getBtnSequenceOfChar(char c) {
		for (CharacterCommandSequence enumVal : CharacterCommandSequence.values()) {
			char currentChar = enumVal.toString().charAt(0);
			if(c == currentChar)
				return enumVal;
		}
		return null;
	}
	
}
