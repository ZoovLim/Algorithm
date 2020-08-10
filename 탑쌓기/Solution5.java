import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution5 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution5.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output5.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution5

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution5
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution5   // 0.5초 수행
       timeout 1 java Solution5     // 1초 수행
 */

class Solution5 {
    static final int max_n = 1000;

    static int n, H;
    static int[] h = new int[max_n], d = new int[max_n - 1];
    static int Answer;


    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input5.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output5.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input5.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output5.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 블록의 개수와 최대 높이를 각각 n, H에 읽어들입니다.
			   그리고 각 블록의 높이를 h[0], h[1], ... , h[n-1]에 읽어들입니다.
			   다음 각 블록에 파인 구멍의 깊이를 d[0], d[1], ... , d[n-2]에 읽어들입니다.
			 */
            stk = new StringTokenizer(br.readLine());
            n = Integer.parseInt(stk.nextToken());
            H = Integer.parseInt(stk.nextToken());
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                h[i] = Integer.parseInt(stk.nextToken());
            }
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < n - 1; i++) {
                d[i] = Integer.parseInt(stk.nextToken());
            }

            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////

            /*
             * 총 시간 복잡도 : theta(Hn), n 이 H 에 비해 매우 크다면, theta(n)
             */

            /*
             * Index는 0을 시작으로 전제
             * a1[i][j] = i + 1 번째 까지의 블록으로 탑을 쌓을 경우, i + 1 번째 블록은 쌓지 않은 상태이며,
             * 더 쌓을 수 있는 높이가 j 인 조합의 수 (0 <= i <= n - 1, 0 <= j <= H)
             *
             * a2[i][j] = i + 1 번째 까지의 블록으로 탑을 쌓을 경우, i + 1 번째 블록은 쌓아져 있는 상태이며,
             * 더 쌓을 수 있는 높이가 j 인 조합의 수 (0 <= i <= n - 1, 0 <= j <= H)
             */
            int[][] a1 = new int[n][H + 1];
            int[][] a2 = new int[n][H + 1];

            /*
             * 블록이 한 개 있을 때, 상태값 초기화
             */
            a1[0][H] = 1;
            a2[0][H - h[0]] = 1;

            /*
             * i = 1 ~ n - 1 까지 최적해 찾기 반복
             * a1[i][j] = a1[i - 1][j] + a2[i - 1][j] : i + 1 번째 블록을 쌓지 않으며, 크기가 i + 1 인 문제의 최적해는
             * 크기 i 인 문제의 모든 조합의 수와 같다.
             *
             * 남아 있는 높이 j (1 ~ H)에 대해서 블록을 더 쌓을 가능성이 있다.
             * 해당 j 에 대해서 이전 크기 문제의 경우의 수가 존재한다면, i + 1 번째 블록을 쌓을 수 있는지 확인해 a2의 경우의 수를 더해준다.
             * 이때, 이전 단계에서 직전 단계의 블록이 쌓여 있을 경우와 그렇지 않은 경우로 분류해 깊이 d 를 고려할지 안할지 결정한다.
             * 해당 최적해는 int 형 범위를 넘는 것을 방지하기 위하여, 1,000,000 을 나눈 나머지 값으로 저장한다.
             * 시간 복잡도 : theta(Hn)
             */
            for (int i = 1; i < n; i++) {
                for (int j = 0; j <= H; j++) {
                    a1[i][j] = (a1[i - 1][j] + a2[i - 1][j]) % 1000000;
                }
                for (int j = 1; j <= H; j++) {
                    if (a1[i - 1][j] != 0) {
                        if (j >= h[i]) {
                            a2[i][j - h[i]] += a1[i - 1][j] % 1000000;
                        }
                    }
                }
                for (int j = 1; j <= H; j++) {
                    if (a2[i - 1][j] != 0) {
                        if (j >= h[i] - d[i - 1]) {
                            a2[i][j - h[i] + d[i - 1]] += a2[i - 1][j] % 1000000;
                        }
                    }
                }
            }

            long tmp = 0;

            /*
             * 남아 있는 높이 0 ~ H - 1 (H는 하나도 안쌓은 경우로 제외) 까지 Case 1, Case 2에 해당하는 a1, a2의 값을 더한다.
             * 시간 복잡도 : theta(H)
             */
            for (int i = 0; i < H; i++) {
                tmp += a1[n - 1][i];
                tmp += a2[n - 1][i];
            }

            Answer = (int) (tmp %= 1000000);

            // output5.txt로 답안을 출력합니다.
            pw.println("#" + test_case + " " + Answer);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
            pw.flush();
        }

        br.close();
        pw.close();
    }
}

