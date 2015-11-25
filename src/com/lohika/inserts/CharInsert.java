package com.lohika.inserts;

/**
 * @author Andrey Zhelezny
 *         Date: 11/24/15
 */
public class CharInsert extends AbstractInsert {
    public CharInsert(String tableName) {
        super(tableName);
        fill(1);
    }

    public CharInsert(String tableName, int size) {
        super(tableName);
        fill(size);

    }

    private void fill(int size) {
        values.clear();
        if (size == 1 || size < 10) {
            add("';'");
            add("'b'");
            add("'U'");
            add("' '");
            add("'c'");
            add("'='");
            add("'M'");
            add("'#'");
            add("'8'");
            add("'K'");
            add("'['");
            add("'6'");
            add("'A'");
            add("'?'");
            add("':'");
            add("'w'");
            add("'2'");
            add("'S'");
            add("'{'");
            add("'k'");
            add("'a'");
            add("'O'");
            add("'`'");
        }
        if (size < 100 && size >= 10) {
            add("';s%>O:c-nq'");
            add("'b,a3o{H/5a'");
            add("'Uf2.Pa(|ms'");
            add("' /Il)Z8,!d'");
            add("'cdZ`  ^`K '");
            add("'=h?V+{$4}h'");
            add("'MaSW<bB?[h'");
            add("'#9mo3yM{Bv'");
            add("'8?ce!a7~]b'");
            add("'Kx27,gIaG%'");
            add("'[.FyT=7#Lf'");
            add("' DP~Ors<YT'");
            add("'A-yVzs:-+I'");
            add("'?JX;XApmM6'");
            add("':B={7Gl}a>'");
            add("'wIaIUT R8o'");
            add("'2<|]CCv?)$'");
            add("'S@sJRSm_`!'");
            add("'{nCYb^y.;o'");
            add("'ktQj$Jr4Y3'");
            add("'aWnU#;.r*4'");
            add("'O9l=l@1Z?0'");
            add("'`2odSK6cP|'");
        }
        if (size > 100) {
            add("';s%>O:c-n,_RTOE;+^^7<ewwH4Au%&-~A%q: (6H} ~;6}ruFn1P|ndPp[;EbtEVh]zLPKF0Kl&l!Pz9V>w$a0+G@=mA;pYbq-T!a%*0!(WJsH(EhT#'");
            add("'b,a3o{H/5a6(T+nxnZOpni/JUsnXVOOj55 wPxFq}-K=ybI7*#>OWx3VUC9}%!iwWk#gYo.ZI#Q*p}JMiiqngMQLo|mI$(_VJK@w@_xKp8TSC5uCvPfr`'");
            add("'Uf2.Pa(|meCE,{F*Y4d</-Mc=n5) v.[{baMM/0RmWln!j481hIs<pg8H;Qv[4S+?43=g$9#syHm]k.8h&eOnB>E1Wa;hB?&]{a@!DE :}3%*>^FaF%'");
            add("' /Il)Z8,!NS&0:~4za$M47wafG7c~m=z>&bQ]M&ho%F&UIo]rn[WtiOYMoa*AxsP,LBeufaJ_icBs|^8-{YIC6h !0Rg|dy(uFvK35N<>nBwm&x]#WZ`'");
            add("'cdZ`hZ^`KLg-v^Y|v$+b{~2;fE]M_!y6s4yv]TwSgWfi%xl-25CvlP7nph#jF5#bFxFB!@uxH2PBRP~3q#HU+^(sws#YdJH?DQS4C;LqgANPPmRrKO'");
            add("'=h?V+{$4}u))Bkp>?l^tO@seD7J7aj0ETYqEO#)1],>A4K*$@aR|ZyKlbN.n|_$<h0C,n^qC`B)6-OvMAMI(*L%Dh,fD6W{Z0u7lGH%CgT4`gHbU(^'");
            add("'MaSW<bB?[%I,Ao$]c`_}@-a%esHn%~$v3k]^C{/fS:bNM]V8NH?$V.SpYX*e;SE*@mBbbj^G<aPz7>X+m[*8[51 BqUuemSSV/9X|ditr:Q;bH#@#a;P'");
            add("'#9mo3yM{B)XqZDl!82KwqnwcV6,k!Em:2ac=&O)$.^l#XwL<;37lYl[WU:nI|!$F3&*xltS+R4&Oj]y~Ik:YX,n0^AW}usvZbq+FP!xK*sVvaZla6y'");
            add("'8?ce!a7~]>oT.`d!(qCo#la+0p:St72dvr=m9BY#y:IKs*<1,BM;s;cM4(55PVsBGW>rWLDM>XC,PeCR=M[kXIJ,2eMN^kF[i/s[D=5]],/csf<.b'");
            add("'Kx27,gIaGmr;ART5.I JqyBuo&5bta%OtD +Eb5i,}gD]ddyiHyBsEojE][.S$yhQj`R6Q<d=hB;V&~f_@xFbN@Hzm.%z;.HYQTZb-{Y}l]&B]+ldvX'");
            add("'[.FyT=7#Lb:K}iaNSF|&M(ibr;+K6<7*JxmSBf5~7tbt?oI4J0R[)eJ#M>[#(U(lkSCjb?ls9Qk4/z:L*tX$D4<[avVf_^k>n9eI>%0XpeuF8V/_,^|]'");
            add("' DP~Ors<YVjnX[6 t1M.NkN%nFI)55DmVBbf ;$n78vVxL_<yYXZ~C;3!6&H4D).3$51{$mJHpfb`S+tGa7Dkt$n)?rz5[@=:S|;tjvgTn,8:4uIYsdZ2'");
            add("'A-yVzs:-+Q,5W1tjWh77YsjvPK]ga#y/HjX^4rq;?,m7]R6z2V%y.SQlP:%H`!Zn0d33B-+M|Q*hKb_x#aIH)Ko@jOHT_5GNc<o1)az<~wQ44WX%01'");
            add("'?JX;XApmM[qc^{!|PT$A$%i(k:oaet]oev^r2(jRka$*7bU%bV@;(PDjx!weV3K*|YGAkbb[qA3V-}EtR0{$Yo=b_yGq`si65:JFz/&2Wq_Ss*9O3L-q'");
            add("':B={7Gl}aD}bo5R4a>uy:CyX>^DfCK]i!rFq)Dko-4A+T|3-Yri>zWYr<U,%ADnZM@)F]~~`;s/>%bLZI>=z 4=,`$(I+`z(|Zb<&5!uzHb5b)~AHXMz'");
            add("'wIaIUT R8kf^tr!S}72/DbK:%<X4z#D7a1{_svc.U3*)9*xowHa_]2gtuOh|Ngvhf1IE649u|@smQbX9E?x>Sc4eNKmU?/:hdKEVL5f)X~%MbP7yo?s'");
            add("'2<|]CCv?))U@-cD[Z0)|:Trbh$?WbS .j$,i3xXo+.DhF:-?M%PL<iTT(tslbr@V@yqY5.y}=vo[nx]2$G@tcSn^-_nT^(ic*>*xClU<B|lCIR#849AU'");
            add("'S@sJRSm_`U0cLuG?G(ar2gFbn>rzQ<7Fd7>C@)&XZndN`pH&_fp^YIq7*Mk&ep<g-LaIcM)W*x!g.{QW$@g[w^[<+8@&`VSY&#-yb..%8%S4E+8<&CB`0'");
            add("'{nCYb^y.;mzf{K3;or77p15I80;~J&`wg2Mx&wj2edE-#/}m|h>0P|E{~vIA+?F%oKO8AFlyA^E-}K0cT+&UbJ o,v1A0A.f4vy9!B`|N.l2YYIO*#Va'");
            add("'ktQj$Jr4YxVqK)L,uJBob]QDpv+>=SiGGQC*9pwrt `;9`zO;(L|fSgG]slN6YRlxC:}%D:{oR,(bJl9GgZUW[@EK}vZO.>Z|<)-7eK3O%hWPEpvyCwZu'");
            add("'aWnU#;.r*4-@Z[XS:?XCEVbul1@/A;E&lpb{l3#6&b=v9?hmT?<zf0!LA[;9kd2KY4^<.$/JEZ6A)4V>u3(`!;6LNNFy;x!Jggg5OKs;2]v]y+:WU@}'");
            add("'O9l=l@1Z?9(gBOR>+/qCR9N_?)E&#0H|i<s crfP<ILl [Kb[ 0]^@&z`1-Gq;4d,21iq~y?0h~r3O>^rS7#c7?Y_fKYa`9x%B]S{KTebXE+g??uPIU}'");
            add("'`2odSK6cP;OeA^]:+tfN_U=-[bkh/G0{()Fg_A~*%HMd&yFaSWe$b3dw!csr7wwO`IaREqdYr&,!W<AxDy!Uy9XYm}glr{$6bgybG,N!4|`a-F$REW4U'");
        }
        add("NULL");
    }
}

