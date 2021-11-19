
export class Globals{
  public static isAuth: boolean = false;
  public static msg: string = "";
  public static userId: number = 0;
  public static user: string = "";
  public static role: string = "";

  public static emptySession(){
    Globals.isAuth = false;
    Globals.msg = "";
    Globals.userId = 0;
    Globals.user = "";
    Globals.role = "";
  }
}
