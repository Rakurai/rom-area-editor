package com.ageoflegacy.aedit.model.area;

public class MudReset {
	char command;
	int arg1, arg2, arg3, arg4;

	public MudReset(char letter, int a1, int a2, int a3, int a4) {
		command = letter;
		arg1 = a1;
		arg2 = a2;
		arg3 = a3;
		arg4 = a4;
	}

	public char getCommand() {
		return command;
	}

	public int getArg(int which) {
		switch (which) {
		case 1:
			return arg1;
		case 2:
			return arg2;
		case 3:
			return arg3;
		case 4:
			return arg4;
		default:
			throw new ArrayIndexOutOfBoundsException("bad value to get arg in resets");
		}
	}

	public void setArg(int which, int newValue) {
		switch (which) {
		case 1:
			arg1 = newValue;
			return;
		case 2:
			arg2 = newValue;
			return;
		case 3:
			arg3 = newValue;
			return;
		case 4:
			arg4 = newValue;
			return;
		default:
			return;
		}
	}

	private String IA(int a) {
		return Integer.toString(a);
	}

	public String toString() {
		String temp;
		switch (command) {
		case 'M': {
			temp = "M 0 " + IA(arg1) + " " + IA(arg2) + " " + IA(arg3) + " " + IA(arg4);
			break;
		}
		case 'O': { // obj // room
			temp = "O 0 " + IA(arg1) + " 0 " + IA(arg3);
			break;
		}
		case 'P': {
			temp = "P 0 " + IA(arg1) + " " + IA(arg2) + " " + IA(arg3) + " " + IA(arg4);
			break;
		}
		case 'G': {
			temp = "G 0 " + IA(arg1) + " 0";
			break;
		}
		case 'E': {
			temp = "E 0 " + IA(arg1) + " 0 " + IA(arg3);
			break;
		}
		case 'D': {
			temp = "D 0 " + IA(arg1) + " " + IA(arg2) + " " + IA(arg3);
			break;
		}
		case 'R': {
			temp = "R 0 " + IA(arg1) + " " + IA(arg3);
			break;
		}
		default: {
			temp = "BAD COMMAND IN RESET.";
			break;
		}
		}
		return temp;
	}

}
