package com.dalongtech.customkeyboard.constants;

/**
 * Author:xianglei
 * Date: 2019-12-03 11:34
 * Description:
 */
public interface KeyConst {

    int KEY_EMPTY = 0;                              //点到空的地方
    int NO_FIND_KEY = -10000;                       //没有找到对应的windows键

    //***************************************基本键及功能键************************************************
    int KEY_LINE_FEED = -1001;                      //换行
    int KEY_SYMBOL = -1002;                         //功能键 切换到符号键盘
    int KEY_FUNCTION_WIN = -1004;                   //功能键 切换到windows特殊符号 区别于按键KEY_WIN
    int KEY_WWW = -1005;                            //www.
    int KEY_COM = -1006;                            //.com
    int KEY_LANGUAGE = -1007;                       //功能键 中/英切换
    int KEY_BACK = -1008;                           //功能键 切换到基本按键
    int KEY_TAB = -1009;                            //tab键
    int KEY_PREVIOUS_PAGE = -1010;                  //上翻一页
    int KEY_NEXT_PAGE = -1011;                      //下翻一页
    int KEY_CAPITAL = -1012;                        //大写键，按键码是-1 判断状态后使用
    int KEY_LOWER_CASE = -1013;                     //小写键，按键码是-1 判断状态后使用
    int KEY_MAIN_AT = -1014;                        //首页@键，因为埋点需求，做区分
    int KEY_MAIN_DOT = -1015;                       //首页. 点键，因为埋点需求，做区分
    int KEY_SYMBOL_LANGUAGE = -1016;                //符号页中英键
    int KEY_SYMBOL_BACK = -1017;                    //符号页返回键
    int KEY_WIN_LANGUAGE = -1018;                   //WIN页中英键
    int KEY_WIN_BACK = -1019;                       //WIN页返回键

    int KEY_AT = 96;                                //@键
    int KEY_SPACE = 32;                             //空格键

    //***************************************符号键************************************************
    int KEY_GRAVE_ACCENT = -2001;                   // ` 重音号 [EN]backquote/grave accent [FR]
    int KEY_TILDE = -2002;                          // ~ 波浪号 tilde
    int KEY_EXCLAMATION_MARK = -2003;               // ! 叹号 [EN]exclamation mark/bang [FR]un point d'exclamation
    int KEY_CROSSHATCH = -2005;                     // # 井号 [EN]crosshatch/sharp/hash [FR]un dièse , un [CAN]carré
    int KEY_DOLLAR_SIGN = -2006;                    // $ 美元符号 [EN]dollar sign [FR]un signe du dollar, un dollar
    int KEY_PERCENT_SIGN = -2007;                   // % 百分号 [EN]percent sign/mod [FR]un signe de pour-cent, un pour-cent
    int KEY_CIRCUMFLEX = -2008;                     // ^ 折音号 circumflex/caret
    int KEY_AND = -2009;                            // & and/和/兼 [EN]and/ampersand [FR]une esperluette, un et commercial, un et anglais
    int KEY_ASTERISK = -2010;                       // * 星号 [EN]asterisk/star [FR]un astérisque
    int KEY_LEFT_PARENTHESES = -2011;               // ( 左圆括号/左小括号 [EN](left|open) parentheses [FR]parenthèses
    int KEY_RIGHT_PARENTHESES = -2012;              // ) 右圆括号/右小括号 [EN](right|close) parentheses [FR]parenthèses
    int KEY_HYPHEN = -2013;                         // - 减号/横线 [EN]hyphen/dash/minus sign/ [FR]un trait d'union [FR]le signe moins
    int KEY_EQUAL_SIGN = -2014;                     // = 等号 [EN]equal sign [FR]un signe égal
    int KEY_UNDERLINE = -2015;                      // _ 下划线 [EN]underline/underscore [FR]un underscore, un souligné
    int KEY_PLUS_SIGN = -2016;                      // + 加号 [EN]plus sign [FR]le signe plus
    int KEY_LEFT_BRACKETS = -2017;                  // [ 左方括号/左中括号 [EN](left|open) brackets [FR]crochets (droits) (m)
    int KEY_RIGHT_BRACKETS = -2018;                 // ] 右方括号/右中括号 [EN](right|close) brackets [FR]crochets (droits) (m)
    int KEY_LEFT_BRACES = -2019;                    // { 左花括号/左大括号 [EN](left|open) braces [FR]accolades
    int KEY_RIGHT_BRACES = -2020;                   // } 右花括号/右大括号 [EN](right|close) braces [FR]accolades
    int KEY_BACKSLASH = -2021;                      // \ 反斜线 [EN]backslash/escape [FR]une barre oblique inverse, un anti slash
    int KEY_VERTICAL_BAR = -2022;                   // | 竖线 [EN]bar/pipe/vertical bar [FR]une barre vertical, opérateur de transfert des données
    int KEY_SEMICOLON = -2023;                      // ; 分号 [EN]semicolon [FR]un point-virgule
    int KEY_COLON = -2024;                          // : 冒号 [EN]colon [FR]les deux points, un deux-points
    int KEY_SINGLE_QUOTE = -2026;                   // ‘ 单引号/撇号 [EN]apostrophe/single quote [FR]une apostrophe
    int KEY_DOUBLE_QUOTE = -2027;                   // ” 双引号 [EN]quotation marks/double quote [FR]guillemets (m)
    int KEY_LESS_THAN = -2028;                      // < 小于号 [EN]less than [FR]un signe inférieur
    int KEY_GREATER_THAN = -2029;                   // > 大于号 [EN]greater than [FR]un signe supérieur
    int KEY_SLASH = -2030;                          // / 斜线 [EN]slash [FR]une barre oblique, trait oblique, un slash
    int KEY_QUESTION_MARK = -2031;                  // ? 问号 [EN]question mark [FR]un point d'interrogation
    int KEY_COMMA = -2032;                          // , comma	逗号
    int KEY_DOT = -2033;                            // .
    int KEY_ARROW_LEFT = -2034;                     // 左方向键
    int KEY_ARROW_RIGHT = -2035;                    // 右方向键
    int KEY_ARROW_UP = -2036;                       // 上方向键
    int KEY_ARROW_DOWN = -2037;                     // 下方向键



    //***************************************win按键************************************************
    int KEY_ESC = -3001;
    int KEY_CTRL_L = -3002;
    int KEY_CTRL_R = -3003;
    int KEY_SHIFT_L = -3004;
    int KEY_SHIFT_R = -3005;
    int KEY_ALT_L = -3006;
    int KEY_ALT_R = -3007;
    int KEY_CAPSLK = -3008;
    int KEY_WIN = -3009;
    int KEY_HOME = -3010;
    int KEY_END = -3011;
    int KEY_PRTSC = -3012;
    int KEY_SCRLK = -3013;
    int KEY_PAUSE = -3014;
    int KEY_INS = -3015;
    int KEY_DEL = -3016;
    int KEY_PGUP = -3017;
    int KEY_PUDN = -3018;
    int KEY_F1 = -3019;
    int KEY_F2 = -3020;
    int KEY_F3 = -3021;
    int KEY_F4 = -3022;
    int KEY_F5 = -3023;
    int KEY_F6 = -3024;
    int KEY_F7 = -3025;
    int KEY_F8 = -3026;
    int KEY_F9 = -3027;
    int KEY_F10 = -3028;
    int KEY_F11 = -3029;
    int KEY_F12 = -3030;

    //***************************************基本键a-zA-Z0-9************************************************
    int KEY_a = 97;
    int KEY_b = 98;
    int KEY_c = 99;
    int KEY_d = 100;
    int KEY_e = 101;
    int KEY_f = 102;
    int KEY_g = 103;
    int KEY_h = 104;
    int KEY_i = 105;
    int KEY_j = 106;
    int KEY_k = 107;
    int KEY_l = 108;
    int KEY_m = 109;
    int KEY_n = 110;
    int KEY_o = 111;
    int KEY_p = 112;
    int KEY_q = 113;
    int KEY_r = 114;
    int KEY_s = 115;
    int KEY_t = 116;
    int KEY_u = 117;
    int KEY_v = 118;
    int KEY_w = 119;
    int KEY_x = 120;
    int KEY_y = 121;
    int KEY_z = 122;

    int KEY_A = 65;
    int KEY_B = 66;
    int KEY_C = 67;
    int KEY_D = 68;
    int KEY_E = 69;
    int KEY_F = 70;
    int KEY_G = 71;
    int KEY_H = 72;
    int KEY_I = 73;
    int KEY_J = 74;
    int KEY_K = 75;
    int KEY_L = 76;
    int KEY_M = 77;
    int KEY_N = 78;
    int KEY_O = 79;
    int KEY_P = 80;
    int KEY_Q = 81;
    int KEY_R = 82;
    int KEY_S = 83;
    int KEY_T = 84;
    int KEY_U = 85;
    int KEY_V = 86;
    int KEY_W = 87;
    int KEY_X = 88;
    int KEY_Y = 89;
    int KEY_Z = 90;

    int KEY_0 = 48;
    int KEY_1 = 49;
    int KEY_2 = 50;
    int KEY_3 = 51;
    int KEY_4 = 52;
    int KEY_5 = 53;
    int KEY_6 = 54;
    int KEY_7 = 55;
    int KEY_8 = 56;
    int KEY_9 = 57;





}
