import java.util.Scanner;

public class CastleDefense {
    public static int x, y, attackD;
    public static int[][] map;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 입력값 받아오기 : 세로, 가로, 공격거리
        y = scanner.nextInt();
        x = scanner.nextInt();
        attackD = scanner.nextInt();

        // 맵 생성 후 맵 읽어오기, 궁수 배치 위해 [y+1][x]로 생성
        map = new int[y + 1][x];
        for (int a = 0; a < y; a++) {
            for (int b = 0; b < x; b++) {
                map[a][b] = scanner.nextInt();
            }
        }

        int temp, maxAttack = -1;

        // Brute-Force로 가로에 궁수 3명 배치해가며 각 케이스 모두 계산, 결과로 받아온 값(temp)과 비교하여 큰 값을 저장한다.
        for (int a = 0; a < x - 2; a++) {
            for (int b = a + 1; b < x - 1; b++) {
                for (int c = b + 1; c < x; c++) {
                    temp = calAttackNum(a, b, c);
                    if (maxAttack < temp)
                        maxAttack = temp;
                }
            }
        }

        System.out.println(maxAttack);

    }

    public static int calAttackNum(int att1, int att2, int att3) {
        int[][] map2 = new int[y][x];   // 궁수 간 중복을 피하고 map 복원을 위해, 죽인 적의 위치를 1로 저장하는 새로운 맵 생성
        int[] data = new int[9];        // 죽이려 하는 적과의 거리, 적의 y좌표, x좌표를 순서대로 저장 (궁수1은 [0]~[2], 궁수2는 [3]~[5], 궁수3은 [6]~[8]
        int[] att = new int[3];         // 궁수 위치를 배열로 저장
        int attackNum = 0;
        int step, distance;

        // 새로운 맵을 0으로 초기화
        for (int a = 0; a < y; a++) {
            for (int b = 0; b < x; b++) {
                map2[a][b] = 0;
            }
        }

        // 궁수 위치 배열 저장
        att[0] = att1;
        att[1] = att2;
        att[2] = att3;

        // 총 y번 적이 아래로 이동 (하는 효과를 내기 위해 i 변수 사용)
        for (int i = 0; i < y; i++) {
            step = 1;                   // 세로로도 다수의 행을 건너뛰어 공격할 수 있으므로 체크를 위해 step 변수 사용
            data[0] = attackD + 1;      // 적을 죽였는지 체크하기 위해 최초에 큰 값으로 저장 : 최소 거리를 죽일거니까
            data[3] = attackD + 1;
            data[6] = attackD + 1;

            // 적이 고정된 상태에서, 각 궁수별로 공격을 진행해 봄. 공격 못할수도 있음
            while (true) {
                if (step > attackD) break;      // 최대 사거리 < 직선거리면 가능한 경우가 없음.
                if (step + i > y) break;        // 맨 끝을 넘어서는 경우 예외처리.

                // 가로를 쭉 훑어본다.
                for (int a = 0; a < x; a++) {
                    // 궁수 3명
                    for (int b = 0; b < 3; b++) {
                        distance = Math.abs(a - att[b]) + step;

                        // 적이 있고, 적과의 거리가 공격 사거리보다 짧거나 같은 경우
                        if (map[y - step - i][a] != 0 && distance <= attackD) {
                            // 만약 이전에 발견한 적보다 가까운 경우 거리와 좌표 저장
                            // 같은 턴에서 더 가까운 경우가 있을 수 있으니깐 일단 공격하진 않음 (거리가 같으면 왼쪽부터 때리기 위해 부등호)
                            if (data[3 * b] > distance) {
                                data[3 * b] = distance;
                                data[3 * b + 1] = y - step - i;
                                data[3 * b + 2] = a;
                            }
                        }
                    }
                }
                step++;
            }

            for (int a = 0; a < 3; a++) {
                //공격한 적이 있다면, map에서 지우고 map2에 기록
                if (data[3 * a] <= attackD) {
                    map2[data[3 * a + 1]][data[3 * a + 2]] = 1;
                    map[data[3 * a + 1]][data[3 * a + 2]] = 0;
                }
            }
        }

        // map 복원. 공격한 적 수도 세기.
        for (int a = 0; a < y; a++) {
            for (int b = 0; b < x; b++) {
                map[a][b] += map2[a][b];
                attackNum += map2[a][b];
            }
        }
        return attackNum;
    }
}
