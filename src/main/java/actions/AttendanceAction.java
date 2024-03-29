package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.AttendanceView;
import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import services.AttendanceService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class AttendanceAction extends ActionBase {

    private AttendanceService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new AttendanceService();

        //メソッドを実行
        invoke();
        service.close();
    }






    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件に勤怠データを取得する
            AttendanceView av = service.findOne(toNumber(getRequestParam(AttributeConst.ATT_ID)));



            //入力された勤怠内容を設定する
            av.setAttendanceDate(toLocalDate(getRequestParam(AttributeConst.ATT_DATE)));



            //勤怠データの更新をする
            List<String> errors = service.update(av);


            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.ATTENDANCE, av); //入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_ATT_EDIT);

            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_ATT, ForwardConst.CMD_INDEX);

            }
        }
    }








    public void create() throws ServletException, IOException {

        System.out.println("＊＊＊＊＊＊＊＊＊＊");

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //勤怠の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;

            System.out.println("＊＊＊＊＊＊＊＊＊＊＋＋＋＋＋");

            if (getRequestParam(AttributeConst.ATT_DATE) == null
                    || getRequestParam(AttributeConst.ATT_DATE).equals("")) {
                day = LocalDate.now();
            } else {
                day = LocalDate.parse(getRequestParam(AttributeConst.ATT_DATE));
            }


            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
            System.out.println("＊＊＊＊＊＊＊＊＊＊AttendanceAction" + ev.getName());

            //パラメータの値をもとに勤怠情報のインスタンスを作成する
            AttendanceView av = new AttendanceView(
                    null,
                    ev, //ログインしている従業員を、勤怠作成者として登録する
                    day,
                    getRequestParam(AttributeConst.ATT_ATTENDANCE_AT_WORK),
                    getRequestParam(AttributeConst.ATT_ATTENDANCE_BREAK_START),
                    getRequestParam(AttributeConst.ATT_ATTENDANCE_END_OF_BREAK),
                    getRequestParam(AttributeConst.ATT_ATTENDANCE_LEAVING_WORK),
                    null,
                    null);

            //勤怠情報登録
            List<String> errors = service.create(av);


            System.out.println("＊＊＊＊＊＊＊＊＊＊｜｜｜｜｜｜");

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.ATTENDANCE, av);//入力された勤怠情報
                putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_ATT_NEW);


            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_ATT, ForwardConst.CMD_INDEX);
            }
       }
        System.out.println("＊＊＊＊＊＊＊＊＊＊＝＝＝＝＝");
    }





    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン


        //勤怠情報の空インスタンスに、日報の日付＝今日の日付を設定する
        AttendanceView av = new AttendanceView();
        av.setAttendanceDate(LocalDate.now());
        putRequestScope(AttributeConst.ATTENDANCE, av); //日付のみ設定済みの日報インスタンス


        //新規登録画面を表示
        forward(ForwardConst.FW_ATT_NEW);

    }






    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */

    public void index() throws ServletException, IOException {



        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }



        //一覧画面を表示
        forward(ForwardConst.FW_ATT_INDEX);

    }


}