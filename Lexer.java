package enshud.s1.lexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Lexer {

	/**
	 * サンプルmainメソッド． 単体テストの対象ではないので自由に改変しても良い．
	 */
	public static void main(final String[] args) {
		// normalの確認
		new Lexer().run("data/pas/normal01.pas", "tmp/out1.ts");
	}

	/**
	 * TODO
	 * 
	 * 開発対象となるLexer実行メソッド． 以下の仕様を満たすこと．
	 * 
	 * 仕様: 第一引数で指定されたpasファイルを読み込み，トークン列に分割する． トークン列は第二引数で指定されたtsファイルに書き出すこと．
	 * 正常に処理が終了した場合は標準出力に"OK"を， 入力ファイルが見つからない場合は標準エラーに"File not found"と出力して終了すること．
	 * 
	 * @param inputFileName  入力pasファイル名
	 * @param outputFileName 出力tsファイル名
	 */
	record Tokens(int id, String type, String name) {
	}

	private Tokens getToken(String name) {
		Tokens res = new Tokens(1, "a", name);
		return res;
	}

	public void run(final String inputFileName, final String outputFileName) {
		// 0. Tokenクラスの作成
		final class Token { // ローカルクラス
			int id;
			String type;
			String name;

			public Token(final String name) {
				this.name = name;
				switch (name) {
				case "and":
					this.type = "SAND";
					this.id = 0;
					break;
				case "array":
					this.type = "SARRAY";
					this.id = 1;
					break;
				case "begin":
					this.type = "SBEGIN";
					this.id = 2;
					break;
				case "boolean":
					this.type = "SBOOLEAN";
					this.id = 3;
					break;
				case "char":
					this.type = "SCHAR";
					this.id = 4;
					break;
				case "div":
					this.type = "SDIVD";
					this.id = 5;
					break;
				case "/":
					this.type = "SDIVD";
					this.id = 5;
					break;
				case "do":
					this.type = "SDO";
					this.id = 6;
					break;
				case "else":
					this.type = "SELSE";
					this.id = 7;
					break;
				case "end":
					this.type = "SEND";
					this.id = 8;
					break;
				case "false":
					this.type = "SFALSE";
					this.id = 9;
					break;
				case "if":
					this.type = "SIF";
					this.id = 10;
					break;
				case "integer":
					this.type = "SINTEGER";
					this.id = 11;
					break;
				case "mod":
					this.type = "SMOD";
					this.id = 12;
					break;
				case "not":
					this.type = "SNOT";
					this.id = 13;
					break;
				case "of":
					this.type = "SOF";
					this.id = 14;
					break;
				case "or":
					this.type = "SOR";
					this.id = 15;
					break;
				case "procedure":
					this.type = "SPROCEDURE";
					this.id = 16;
					break;
				case "program":
					this.type = "SPROGRAM";
					this.id = 17;
					break;
				case "readln":
					this.type = "SREADLN";
					this.id = 18;
					break;
				case "then":
					this.type = "STHEN";
					this.id = 19;
					break;
				case "true":
					this.type = "STRUE";
					this.id = 20;
					break;
				case "var":
					this.type = "SVAR";
					this.id = 21;
					break;
				case "while":
					this.type = "SWHILE";
					this.id = 22;
					break;
				case "writeln":
					this.type = "SWRITELN";
					this.id = 23;
					break;
				case "=":
					this.type = "SEQUAL";
					this.id = 24;
					break;
				case "<>":
					this.type = "SNOTEQUAL";
					this.id = 25;
					break;
				case "<":
					this.type = "SLESS";
					this.id = 26;
					break;
				case "<=":
					this.type = "SLESSEQUAL";
					this.id = 27;
					break;
				case ">=":
					this.type = "SGREATEQUAL";
					this.id = 28;
					break;
				case ">":
					this.type = "SGREAT";
					this.id = 29;
					break;
				case "+":
					this.type = "SPLUS";
					this.id = 30;
					break;
				case "-":
					this.type = "SMINUS";
					this.id = 31;
					break;
				case "*":
					this.type = "SSTAR";
					this.id = 32;
					break;
				case "(":
					this.type = "SLPAREN";
					this.id = 33;
					break;
				case ")":
					this.type = "SRPAREN";
					this.id = 34;
					break;
				case "[":
					this.type = "SLBRACKET";
					this.id = 35;
					break;
				case "]":
					this.type = "SRBRACKET";
					this.id = 36;
					break;
				case ";":
					this.type = "SSEMICOLON";
					this.id = 37;
					break;
				case ":":
					this.type = "SCOLON";
					this.id = 38;
					break;
				case "..":
					this.type = "SRANGE";
					this.id = 39;
					break;
				case ":=":
					this.type = "SASSIGN";
					this.id = 40;
					break;
				case ",":
					this.type = "SCOMMA";
					this.id = 41;
					break;
				case ".":
					this.type = "SDOT";
					this.id = 42;
					break;
				default:
					char c = name.charAt(0);
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
						// 識別子の場合
						this.type = "SIDENTIFIER";
						this.id = 43;
						break;
					} else if (c >= '0' && c <= '9') {
						// 符号なし整数の場合
						this.type = "SCONSTANT";
						this.id = 44;
						break;
					} else if (c == '\'') {
						// 文字列の場合
						this.type = "SSTRING";
						this.id = 45;
						break;
					}
					// 単独で!などがでてきた場合 : その文字だけ表示?
				}
			}
		}

		// 1. inputFileNameの内容を1文字ずつ読み込む
		// 2. トークンに分割しながらoutputFileNameに書き込む

		// オートマトンの状態
		enum CurrentState {
			INITIAL, // 初期状態
			SYMBOL, // 特殊文字読み込み状態
			IDENTIFIER, // 綴り記号 or 変数名読み込み状態
			CONSTANT, // 符号なし整数読み込み状態
			STRING, // 文字列読み込み状態
			ANNOTATION // 注釈要素読み込み状態
		}
		;

		// cs(新たに読み込んだ文字)の種類を判別
		final class csStateConsider {
			String csState;

			public csStateConsider(final char cs) {
				switch (cs) {
				case '=':
					this.csState = "symbol";
					break;
				case '<':
					this.csState = "symbol";
					break;
				case '>':
					this.csState = "symbol";
					break;
				case '+':
					this.csState = "symbol";
					break;
				case '-':
					this.csState = "symbol";
					break;
				case '*':
					this.csState = "symbol";
					break;
				case '(':
					this.csState = "symbol";
					break;
				case ')':
					this.csState = "symbol";
					break;
				case '[':
					this.csState = "symbol";
					break;
				case ']':
					this.csState = "symbol";
					break;
				case ';':
					this.csState = "symbol";
					break;
				case ':':
					this.csState = "symbol";
					break;
				case '.':
					this.csState = "symbol";
					break;
				case ',':
					this.csState = "symbol";
					break;
				// csが'
				case '\'':
					this.csState = "singlequatation";
					break;
				// csが{
				case '{':
					this.csState = "annotationStart";
					break;
				// csが}
				case '}':
					this.csState = "annotationFinish";
					break;
				// csが注釈以外のトークン分離子
				case ' ':
					this.csState = "separate";
					break;
				case '\t':
					this.csState = "separate";
					break;
				case '\n':
					this.csState = "separate";
					break;
				default:
					// csが英字or数字
					if ((cs >= 'a' && cs <= 'z') || (cs >= 'A' && cs <= 'Z') || (cs >= '0' && cs <= '9')) {
						this.csState = "charORint";
						break;
					} else {
						this.csState = "other";
						break;
					}
				}
			}
		}

		final class acceptToken {

			String w;

			public acceptToken(final Token token, final String outputFileName, final int currentLine) {
				this.w = token.name + "\t" + token.type + "\t" + token.id + "\t"
						+ Integer.valueOf(currentLine).toString();
				try {
					File file = new File(outputFileName);
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
					System.out.println(this.w);
					pw.println(this.w);
					pw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		try {
			File file = new File(inputFileName);
			FileReader filereader = new FileReader(file);

			int c;
			CurrentState currentState = CurrentState.INITIAL; // 初期状態
			int lineCount = 1;
			char ps = '\0';
			String rs = null;

			while ((c = filereader.read()) != -1) {
				final char cs = (char) c;
				csStateConsider state = new csStateConsider(cs);

				if (state.csState == "symbol") {
					switch (currentState) {
					case INITIAL:
						if ((cs == '<') || (cs == '>') || (cs == ':') || (cs == '.')) {
							// 先読みが必要な場合
							currentState = CurrentState.SYMBOL;
							rs += String.valueOf(cs);
							ps = cs;
							break;
						} else {
							// 受理
							rs += String.valueOf(cs);
							acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
							currentState = CurrentState.INITIAL;
							rs = null;
							ps = cs;
							break;
						}
					case SYMBOL: // 先読み判定
						if (ps == '<') {
							if (cs == '>' || cs == '=') {
								// 受理
								rs += String.valueOf(cs);
								acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
								currentState = CurrentState.INITIAL;
								rs = null;
								ps = cs;
								break;
							} else {
								// psまでで受理
								acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
								currentState = CurrentState.SYMBOL;
								rs = String.valueOf(cs);
								ps = cs;
								break;
							}
						} else if (ps == '>') {
							if (cs == '=') {
								// 受理
								rs += String.valueOf(cs);
								acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
								currentState = CurrentState.INITIAL;
								rs = null;
								ps = cs;
								break;
							} else {
								// psまでで受理
								acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
								currentState = CurrentState.SYMBOL;
								rs = String.valueOf(cs);
								ps = cs;
							}
						} else if (ps == ':') {
							if (cs == '=') {
								// 受理
								rs += String.valueOf(cs);
								acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
								currentState = CurrentState.INITIAL;
								rs = null;
								ps = cs;
								break;
							} else {
								// psまでで受理
								acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
								currentState = CurrentState.SYMBOL;
								rs = String.valueOf(cs);
								ps = cs;
							}
						} else if (ps == '.') {
							if (cs == '.') {
								// 受理
								rs += String.valueOf(cs);
								acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
								currentState = CurrentState.INITIAL;
								rs = null;
								ps = cs;
								break;
							} else {
								// psまでで受理
								acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
								currentState = CurrentState.SYMBOL;
								rs = String.valueOf(cs);
								ps = cs;
							}
						}
					case IDENTIFIER: // 名前確定
					case CONSTANT: // 数字確定
						// psまでで受理
						acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
						currentState = CurrentState.SYMBOL;
						rs = String.valueOf(cs);
						ps = cs;
					case STRING:
					case ANNOTATION:
						// 読み飛ばし
						ps = cs;
						break;
					default:
						break;
					}
				} else if (state.csState == "charORint") {
					switch (currentState) {
					case INITIAL: // 英字ならばIDENTIFIER, 数字ならCONSTANTへ
						if (cs >= '0' && cs <= '9') {
							currentState = CurrentState.CONSTANT;
						} else {
							currentState = CurrentState.IDENTIFIER;
						}
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					case SYMBOL:
					case CONSTANT:
						if (cs >= '0' && cs <= '9') {
							// 継続
							rs += String.valueOf(cs);
							ps = cs;
							break;
						} else {
							// psまでで受理
							acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
							currentState = CurrentState.IDENTIFIER;
							rs = String.valueOf(cs);
							ps = cs;
							break;
						}
					case IDENTIFIER:
					case STRING:
					case ANNOTATION:
						// 読み飛ばし
						ps = cs;
						break;
					default:
						break;
					}
				} else if (state.csState == "singlequatation") {
					switch (currentState) {
					case INITIAL:
						currentState = CurrentState.STRING;
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					case SYMBOL:
					case IDENTIFIER:
					case CONSTANT:
						// psまでで受理
						acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
						currentState = CurrentState.IDENTIFIER;
						rs = String.valueOf(cs);
						ps = cs;
						break;
					case STRING:
						// 文法的に正しいかは無視(1文字以上の文字列要素があるかは加味しない)
						// 受理
						rs += String.valueOf(cs);
						acceptToken write1 = new acceptToken(new Token(rs), outputFileName, lineCount);
						currentState = CurrentState.INITIAL;
						rs = null;
						ps = cs;
						break;
					case ANNOTATION:
						// 読み飛ばし
						ps = cs;
						break;
					default:
					}
				} else if (state.csState == "separate") {
					switch (currentState) {
					case INITIAL:
						// 継続(仮トークン更新なし)
						ps = cs;
						if (cs == '\n') {
							lineCount += 1;
						}
						break;
					case SYMBOL:
					case IDENTIFIER:
					case CONSTANT:
						// psまでで受理
						acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
						currentState = CurrentState.INITIAL;
						rs = String.valueOf(cs);
						ps = cs;
						if (cs == '\n') {
							lineCount += 1;
						}
						break;
					case STRING:
						// csが\nであっても, 文字列として継続する
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						if (cs == '\n') {
							lineCount += 1;
						}
						break;
					case ANNOTATION:
						// 読み飛ばし
						ps = cs;
						if (cs == '\n') {
							lineCount += 1;
						}
						break;
					default:
					}
				} else if (state.csState == "annotationStart") {
					switch (currentState) {
					case INITIAL:
						// ANNOTATIONへ状態遷移
						currentState = CurrentState.ANNOTATION;
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					case SYMBOL:
					case IDENTIFIER:
					case CONSTANT:
						// psまでで受理
						acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
						currentState = CurrentState.ANNOTATION;
						rs = String.valueOf(cs);
						ps = cs;
						break;
					case STRING:
					case ANNOTATION:
						// 読み飛ばし
						ps = cs;
						break;
					default:
					}
				} else if (state.csState == "annotationFinish") {
					switch (currentState) {
					case INITIAL:
						// 注釈として読みとばす. 初期状態へ.
						ps = cs;
						rs = null;
						currentState = CurrentState.INITIAL;
						break;
					case SYMBOL:
					case IDENTIFIER:
					case CONSTANT:
						// psまでで受理
						acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
						currentState = CurrentState.INITIAL;
						rs = null;
						ps = cs;
						break;
					case STRING:
						// 継続
						rs += String.valueOf(cs);
						ps = cs;
						break;
					case ANNOTATION:
						// 読み飛ばし, 初期状態へ
						ps = cs;
						rs = null;
						currentState = CurrentState.INITIAL;
						break;
					default:
					}
				} else if (state.csState == "other") {
					switch (currentState) {
					case INITIAL:
						// 読み飛ばす
						ps = cs;
						break;
					case SYMBOL:
					case IDENTIFIER:
					case CONSTANT:
						// psまでで受理した後読み飛ばす
						acceptToken write = new acceptToken(new Token(rs), outputFileName, lineCount);
						currentState = CurrentState.INITIAL;
						rs = null;
						ps = cs;
						break;
					case STRING:
					case ANNOTATION:
						// 読み飛ばす
						ps = cs;
						break;
					default:
					}
				}
			}

		} catch (FileNotFoundException ex) {
			System.err.println("File not found");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// 4. メッセージ出力
		System.out.println("OK");
	}
}
