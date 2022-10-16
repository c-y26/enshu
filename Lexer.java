package enshud.s1.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.text.AsyncBoxView.ChildState;

public class Lexer {

	/**
	 * サンプルmainメソッド．
	 * 単体テストの対象ではないので自由に改変しても良い．
	 */
	public static void main(final String[] args) {
		// normalの確認
		new Lexer().run("data/pas/normal01.pas", "tmp/out1.ts");
		new Lexer().run("data/pas/normal02.pas", "tmp/out2.ts");
		new Lexer().run("data/pas/normal03.pas", "tmp/out3.ts");
	}

	/**
	 * TODO
	 * 
	 * 開発対象となるLexer実行メソッド．
	 * 以下の仕様を満たすこと．
	 * 
	 * 仕様:
	 * 第一引数で指定されたpasファイルを読み込み，トークン列に分割する．
	 * トークン列は第二引数で指定されたtsファイルに書き出すこと．
	 * 正常に処理が終了した場合は標準出力に"OK"を，
	 * 入力ファイルが見つからない場合は標準エラーに"File not found"と出力して終了すること．
	 * 
	 * @param inputFileName 入力pasファイル名
	 * @param outputFileName 出力tsファイル名
	 */
	public void run(final String inputFileName, final String outputFileName) {
		// 0. Tokenクラスの作成
		final class Token {

			TokenType type;

			public Token(String name){
				this.type = TokenType.getTokenType(name);
			}
		}

		final enum TokenType {

			SAND() {
				@Override int getID() { return 0; }
				@Override String getName() { return "and"; }
			},

			SARRAY() {
				@Override int getID() { return 1; }
				@Override String getName() { return "array"; }
			},

			SBEGIN() {
				@Override int getID() { return 2; }
				@Override String getName() { return "begin"; }
			},

			SBOOLEAN() {
				@Override int getID() { return 3; }
				@Override String getName() { return "boolean"; }
			},

			SCHAR() {
				@Override int getID() { return 4; }
				@Override String getName() { return "char"; }
			},

			SDIVD(name) {
				@Override int getID() { return 5; }
				@Override String getName() { return name; }
			},

			SDO() {
				@Override int getID() { return 6; }
				@Override String getName() { return "do"; }
			},

			SELSE() {
				@Override int getID() { return 7; }
				@Override String getName() { return "else"; }
			},

			SEND() {
				@Override int getID() { return 8; }
				@Override String getName() { return "end"; }
			},

			SFALSE() {
				@Override int getID() { return 9; }
				@Override String getName() { return "false"; }
			},

			SIF() {
				@Override int getID() { return 10; }
				@Override String getName() { return "if"; }
			},

			SINTEGER() {
				@Override int getID() { return 11; }
				@Override String getName() { return "integer"; }
			},

			SMOD() {
				@Override int getID() { return 12; }
				@Override String getName() { return "mod"; }
			},

			SNOT() {
				@Override int getID() { return 13; }
				@Override String getName() { return "not"; }
			},

			SOF() {
				@Override int getID() { return 14; }
				@Override String getName() { return "of"; }
			},

			SOR() {
				@Override int getID() { return 15; }
				@Override String getName() { return "or"; }
			},

			SPROCEDURE() {
				@Override int getID() { return 16; }
				@Override String getName() { return "procedure"; }
			},

			SPROGRAM() {
				@Override int getID() { return 17; }
				@Override String getName() { return "program"; }
			},

			SREADLN() {
				@Override int getID() { return 18; }
				@Override String getName() { return "readln"; }
			},

			STHEN() {
				@Override int getID() { return 19; }
				@Override String getName() { return "then"; }
			},

			STRUE() {
				@Override int getID() { return 20; }
				@Override String getName() { return "true"; }
			},

			SVAR() {
				@Override int getID() { return 21; }
				@Override String getName() { return "var"; }
			},

			SWHILE() {
				@Override int getID() { return 22; }
				@Override String getName() { return "while"; }
			},

			SWRITELN() {
				@Override int getID() { return 23; }
				@Override String getName() { return "writeln"; }
			},

			SEQUAL() {
				@Override int getID() { return 24; }
				@Override String getName() { return "="; }
			},

			SNOTEQUAL() {
				@Override int getID() { return 25; }
				@Override String getName() { return "<>"; }
			},

			SLESS() {
				@Override int getID() { return 26; }
				@Override String getName() { return "<"; }
			},

			SLESSEQUAL() {
				@Override int getID() { return 27; }
				@Override String getName() { return "<="; }
			},

			SGREATEQUAL() {
				@Override int getID() { return 28; }
				@Override String getName() { return ">="; }
			},

			SGREAT() {
				@Override int getID() { return 29; }
				@Override String getName() { return ">"; }
			},

			SPLUS() {
				@Override int getID() { return 30; }
				@Override String getName() { return "+"; }
			},

			SMINUS() {
				@Override int getID() { return 31; }
				@Override String getName() { return "-"; }
			},

			SSTAR() {
				@Override int getID() { return 32; }
				@Override String getName() { return "*"; }
			},

			SLPAREN() {
				@Override int getID() { return 33; }
				@Override String getName() { return "("; }
			},

			SRPAREN() {
				@Override int getID() { return 34; }
				@Override String getName() { return ")"; }
			},

			SLBRACKET() {
				@Override int getID() { return 35; }
				@Override String getName() { return "["; }
			},

			SRBRACKET() {
				@Override int getID() { return 36; }
				@Override String getName() { return "]"; }
			},

			SSEMICOLON() {
				@Override int getID() { return 37; }
				@Override String getName() { return ";"; }
			},

			SCOLON() {
				@Override int getID() { return 38; }
				@Override String getName() { return ":"; }
			},

			SRANGE() {
				@Override int getID() { return 39; }
				@Override String getName() { return ".."; }
			},

			SASSIGN() {
				@Override int getID() { return 40; }
				@Override String getName() { return ":="; }
			},

			SCOMMA() {
				@Override int getID() { return 41; }
				@Override String getName() { return ","; }
			},

			SDOT() {
				@Override int getID() { return 42; }
				@Override String getName() { return "."; }
			},

			SIDENTIFIER(name) {
				@Override int getID() { return 43; }
				@Override String getName() { return name; }
			},

			SCONSTANT(name) {
				@Override int getID() { return 44; }
				@Override String getName() { return name; }
			},

			SSTRING(name) {
				@Override int getID() { return 45; }
				@Override String getName() { return name; }
			};
			
			abstract int getID();
			abstract String getName();

			static TokenType getTokenType(String name) {
				switch (name) {
					case "and": return SAND; break;
					case "array": return SARRAY; break;
					case "begin": return SBEGIN; break;
					case "boolean": return SBOOLEAN; break;
					case "char": return SCHAR; break;
					case "div": return SDIVD("div"); break; 
					case "/": return SDIVD("/"); break;   
					case "do": return SDO; break;
					case "else": return SELSE; break;
					case "end": return SEND; break;
					case "false": return SFALSE; break;
					case "if": return SIF; break;
					case "integer": return SINTEGER; break;
					case "mod": return SMOD; break;
					case "not": return SNOT; break;
					case "of": return SOF; break;
					case "or": return SOR; break;
					case "procedure": return SPROCEDURE; break;
					case "program": return SPROGRAM; break;
					case "readln": return SREADLN; break;
					case "then": return STHEN; break;
					case "true": return STRUE; break;
					case "var": return SVAR; break;
					case "while": return SWHILE; break;
					case "writeln": return SWRITELN; break;
					case "=": return SEQUAL; break;
					case "<>": return SNOTEQUAL; break;
					case "<": return SLESS; break;
					case "<=": return SLESSEQUAL; break;
					case ">=": return SGREATEQUAL; break;
					case ">": return SGREAT; break;
					case "+": return SPLUS; break;
					case "-": return SMINUS; break;
					case "*": return SSTAR; break;
					case "(": return SLPAREN; break;
					case ")": return SRPAREN; break;
					case "[": return SLBRACKET; break;
					case "]": return SRBRACKET; break;
					case ";": return SSEMICOLON; break;
					case ":": return SCOLON; break;
					case "..": return SRANGE; break;
					case ":=": return SASSIGN; break;
					case ",": return SCOMMA; break;
					case ".": return SDOT; break;
					default: 
						char c = name.charAt(0); 
						if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
							// 識別子の場合
							return SIDENTIFIER(name); break;
						} else if(c >= '0' && c <= '9') {
							// 符号なし整数の場合
							return SCONSTANT(name); break;
						} else if(c == '\'') {
							// 文字列の場合
							return SSTRING(name); break;
						} else {
							return null;
						}
				}
			} 
		}


		
		// 1. inputFileNameの内容を読み込む
		try {
			File readfile = new File(inputFileName);
			BufferedReader br = null;
			
			if (readfile.exists()) {
				FileReader fr = new FileReader(inputFileName);
	            br = new BufferedReader(fr);
			} else {
				System.err.println("File not found");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// 2. 読み込んだpasファイルをトークン列に分割し, そのたびにoutputFileNameに書き込む.
		State currentState = State.INITIAL;	// 初期状態
		int c;
		int lineCount = 1;
		char ps = (Character) null;	// 1つ前に読み込んだ文字
		String rs = null;	// 仮トークン
		
		public enum State {
			INITIAL,	// 初期状態 
			SYMBOL,		// 特殊文字読み込み状態
			IDENTIFIER,	// 綴り記号 or 変数名読み込み状態
			CONSTANT,	// 符号なし整数読み込み状態
			STRING,		// 文字列読み込み状態
			ANNOTATION	// 注釈要素読み込み状態
		};

		while ((c = fr.read()) != -1) {
			// cs; 新たに読み込んだ文字
			char cs = (char) c;
			String state = csStateConsider(cs);
			
			// オートマトンで受理するかどうかを判定
			if(state == "symbol") {	// cs が特殊記号の場合
				switch (currentState) {
					case INITIAL:	// 受理 or SYMBOLへ
						if((cs == '<') || (cs == '>') || (cs == ':') || (cs == '.')) {
							// 先読みが必要な場合
							currentState = State.SYMBOL;
							rs += String.valueOf(cs);
							ps = cs;
							break;
						} else {
							// 受理
							rs += String.valueOf(cs);
							acceptToken(rs, outputFileName, lineCount);
							currentState = State.INITIAL;
							rs = null;
							ps = cs;
							break;
						}
					case SYMBOLE:	// 先読み判定
						if(ps == '<') {
							if(cs == '>' || cs == '=') {
								// 受理
								rs += String.valueOf(cs);
								acceptToken(rs, outputFileName, lineCount);
								currentState = State.INITIAL;
								rs = null;
								ps = cs;
								break;
							} else {
								// psまでで受理
								acceptToken(rs, outputFileName, lineCount);
								currentState = State.SYMBOL;
								rs = String.valueOf(cs);
								ps = cs;
								break;
							}
						} else if (ps == '>') {
							if(cs == '=') {
								// 受理
								rs += String.valueOf(cs);
								accept(rs, outputFileName, lineCount);
								currentState = State.INITIAL;
								rs = null;
								ps = cs;
								break;
							} else {
								// psまでで受理
								accept(rs, outputFileName, lineCount);
								currentState = State.SYMBOL;
								rs = String.valueOf(cs);
								ps = cs;
								break;
							}
						} else if(ps ==':') {
							if(cs == '=') {
								// 受理
								rs += String.valueOf(cs);
								accept(rs, outputFileName, lineCount);
								currentState = State.INITIAL;
								rs = null;
								ps = cs;
								break;
							} else {
								// psまでで受理
								accept(rs, outputFileName, lineCount);
								currentState = State.SYMBOL;
								rs = String.valueOf(cs);
								ps = cs;
								break;
							}
						} else if(ps == '.') {
							if(cs == '.') {
								// 受理
								rs += String.valueOf(cs);
								accept(rs, outputFileName, lineCount);
								currentState = State.INITIAL;
								rs = null;
								ps = cs;
								break;
							} else {
								// psまでで受理
								accept(rs, outputFileName, lineCount);
								currentState = State.SYMBOL;
								rs = String.valueOf(cs);
								ps = cs;
								break;
							}
						} else {
							exit(0);
						}
					case IDENTIFIER:	// 名前確定
					case CONSTANT:		// 数字確定
						// psまでで受理
						accept(rs, outputFileName, lineCount);
						currentState = State.SYMBOL;
						rs = String.valueOf(cs);
						ps = cs;
						break;
					case STRING:		
					case ANNOTATION: 	
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					default:			
						exit(0);
				}
			} else if (state == "charORint") {	// csが英字or数字のとき
				switch (currentState) {
					case currentState:	// 英字ならばIDENTIFIER, 数字ならCONSTANTへ
						if(cs >= '0' && cs <= '9') {
							currentState = State.CONSTANT;
						} else {
							currentState = State.IDENTIFIER;
						}
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					case SYMBOLE:
					case CONSTANT:	
						if(cs >= '0' && cs <= '9') {
							// 継続
							rs += String.valueOf(cs);
							ps = cs;
							break;
						} else {
							// psまでで受理
							accept(rs, outputFileName, lineCount);
							currentState = State.IDENTIFIER;
							rs = String.valueOf(cs);
							ps = cs;
							break;
						}
					case IDENTIFIER:
					case STRING:		
					case ANNOTATION:	
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					default:			
						exit(0);
				}
			} else if(state == "singlequatation") {	// csが'のとき
				switch (currentState) {	
					case currentState:	
						currentState = State.STRING;
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					case SYMBOLE:	
					case IDENTIFIER:
					case CONSTANT:
						// psまでで受理
						accept(rs, outputFileName, lineCount);
						currentState = State.IDENTIFIER;
						rs = String.valueOf(cs);
						ps = cs;
						break;
					case STRING:		
						if(rs.length() >= 2) { // 仮トークンの長さが2以上
							// 受理
							rs += String.valueOf(cs);
							accept(rs, outputFileName, lineCount);
							currentState = State.INITIAL;
							rs = null;
							ps = cs;
							break;
						} else {
							exit(0);
						}
					case ANNOTATION: 	
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					default:			
						exit(0);
				}
			} else if (state == "separate") {	// csが注釈以外のトークン分離子のとき
				switch (currentState) {
					case currentState:	
						// 継続(仮トークン更新なし)
						ps = cs;
						if(cs == '\n') { lineCount += 1; }
						break;
					case SYMBOLE:	
					case IDENTIFIER:	
					case CONSTANT:		
						// psまでで受理
						accept(rs, outputFileName, lineCount);
						currentState = State.INITIAL;
						rs = null;
						ps = cs;
						if(cs == '\n') { lineCount += 1; }
						break;
					case STRING:		
						if(cs == '\n') {
							// エラー?
							exit(0);
						} else {
							// 継続
							rs += String.valueOf(cs);
							ps = cs;
							if(cs == '\n') { lineCount += 1; }
							break;
						}
					case ANNOTATION:	
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						if(cs == '\n') { lineCount += 1; }
						break;
					default:			
						exit(0);
				}
			} else if(state == "annotationStart") {	// csが{のとき
				switch(currentState) {
					case currentState:	
						// ANNOTATIONへ状態遷移
						currentState = State.ANNOTATION;
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					case SYMBOLE:	
					case IDENTIFIER:	
					case CONSTANT:		
						// psまでで受理
						accept(rs, outputFileName, lineCount);
						currentState = State.ANNOTATION;
						rs = String.valueOf(cs);
						ps = cs;
						break;
					case STRING:		
					case ANNOTATION:	
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					default:
						exit(0);
				}	
			} else if(state == "annotationFinish") {	//csが}のとき
				switch(currentState) {
					case currentState:	
						exit(0);
					case SYMBOLE:	
					case IDENTIFIER:	
					case CONSTANT:		
						// psまでで受理
						accept(rs, outputFileName, lineCount);
						currentState = State.INITIAL;
						rs = String.valueOf(cs);
						ps = cs;
						break;
					case STRING:		
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					case ANNOTATION:
						// 受理
						rs += String.valueOf(cs);
						accept(rs, outputFileName, lineCount);
						currentState = State.INITIAL;
						rs = null;
						ps = cs;
						break;	
					default:
						exit(0);
				}		
			} else if(state == "other") {	// csが上記以外の文字のとき
				switch(currentState) {
					case currentState:	
						exit(0);
					case SYMBOLE:	
					case IDENTIFIER:	
					case CONSTANT:		
						// psまでで受理
						accept(rs, outputFileName, lineCount);
						currentState = State.INITIAL;
						rs = String.valueOf(cs);
						ps = cs;
						break;
					case STRING:		
					case ANNOTATION:	
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break; 
					default:
						exit(0);
				}		
			}
		}

		// 4. メッセージ出力
		System.out.println("ok");

		// csの種類を判定する関数.
		public static String csStateConsider (char cs) {
			String csState;
			// csが特殊文字
			switch (cs) {
				case '=': csState = "symbol"; break;
				case '<': csState = "symbol"; break;
				case '>': csState = "symbol"; break;
				case '+': csState = "symbol"; break;
				case '-': csState = "symbol"; break;
				case '*': csState = "symbol"; break;
				case '(': csState = "symbol"; break;
				case ')': csState = "symbol"; break;
				case '[': csState = "symbol"; break;
				case ']': csState = "symbol"; break;
				case ';': csState = "symbol"; break;
				case ':': csState = "symbol"; break;
				case '.': csState = "symbol"; break;
				case ',': csState = "symbol"; break;
				// csが'
				case '\'': csState = "singlequatation"; break;
				// csが{
				case '{': csState = "annotationStart"; break;
				//	csが}			
				case '}': csState = "annotationFinish"; break;
				// csが注釈以外のトークン分離子
				case ' ': csState = "separate"; break;
				case '\t': csState = "separate"; break;
				case '\n': csState = "separate"; break;
				default:
				// csが英字or数字
					if((cs >= 'a' && cs <= 'z') || (cs >= 'A' && cs <= 'Z') || (cs >= '0' && cs <= '9')) {
						csState = "charORint";
					} else {
						csState = "other";
					}
			}
			return csState;
		}

		// 3. トークン列をtsファイルに書き出す.
		public static void acceptToken(String rs, String outputFileName, int lineCount) {
			
			Token token = Token(rs);
			String w = token.getName() + "\t" + token.name() + "\t" + token.getID() + "\t" 
			+ toString(Integer.valueOf(lineCount));
			try{
				FileWriter fw = new FileWriter(outputFileName);
				PrintWriter pw = new PrintWriter(new BufferedReader(fw));
				pw.println(w);
				pw.close();
			} catch (IOException ex) {
					ex.printStackTrace();
			}
		}

}
