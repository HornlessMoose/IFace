
import java.util.Scanner;
public class Main {
    private static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {

        String[][] userProfiles = new String[200][6]; // LOGIN == 0
                                                      // PASSWORD == 1
                                                      // NAME == 2
                                                      // ADDRESS == 3
                                                      // EMAIL = 4
                                                      // COMMUNITY = 5

        initializeProfiles(userProfiles);

        int[][] friendships = new int[200][200];      // NO FRIENDS == 0
                                                      // FRIENDSHIP PENDING == 1
                                                      // FRIENDS == 2
        initializeFriendships(friendships);

        String[][] friendsMessages = new String[200][200];
        initializeMessages(friendsMessages);

        String[][] communities = new String[200][203]; // i = 0; i < 200; i++
                                                       // j = 3; j < 203; j++
                                                       // COMMUNITIES [i][0] == OWNER EMAIL(ID)
                                                       // COMMUNITIES [i][1] == COMMUNITY NAME
                                                       // COMMUNITIES [i][2] == COMMUNITY DESCRIPTION
                                                       // COMMUNITIES [i][j] == MEMBERS EMAIL


        initializeCommunities(communities);


        menu();
        int choice = Integer.parseInt(input.nextLine()), currentSize = 0, position, loggedOptions;
        String name, login, password;


        while (choice != -1) {



            switch (choice) {
                case 1:

                    System.out.println("Informe seu login:");

                    do {
                        login = input.nextLine();

                    }while(verifyLogin(login, userProfiles, currentSize));

                    position = getEmptyPosition(userProfiles);

                    System.out.println("seu nome:");
                    name = input.nextLine();
                    System.out.println("sua senha:");
                    password = input.nextLine();
                    createAccount(position, userProfiles, login, name, password);
                    currentSize++;



                    break;
                case 2:
                    System.out.println("Login:");

                    do{
                        login = input.nextLine();
                        position = validLogin(userProfiles, login);
                    }while (position == -1);
                    System.out.println("Senha:");
                    password = input.nextLine();

                    while(!userProfiles[position][1].equals(password)){
                        System.out.println("Senha incorreta!\ninforme outra senha:");
                        password = input.nextLine();
                    }

                    System.out.println("logado com sucesso!\n");


                    loggedOptions = 1;
                    while(loggedOptions != -1){
                        loggedMenu();
                        loggedOptions = Integer.parseInt(input.nextLine());

                        switch (loggedOptions){
                            case 1:
                                editProfile(position, userProfiles);
                                break;
                            case 2:
                                addFriend(position, userProfiles, friendships);
                                break;
                            case 3:
                                sendMessage(position, userProfiles, friendships, friendsMessages);
                                break;
                            case 4:
                                friendshipRequest(position, userProfiles, friendships);
                                break;
                            case 5:
                                seeProfile(position, userProfiles);
                                break;
                            case 6:
                                seeMessages(position, userProfiles, friendsMessages, friendships);
                                break;
                            case 7:
                                createCommunity(position, userProfiles, communities);
                                break;
                            case 8:
                                joinCommunity(position, userProfiles, communities);
                                break;
                            case 9:
                                seeFriends(position, userProfiles, friendships);
                                break;
                            case 10:
                                seeCommunities(position, userProfiles, communities);
                                break;
                            case 11:
                                loggedOptions = deleteAccount(position, userProfiles, communities, friendships, friendsMessages, loggedOptions);
                            case -1:
                                break;
                            default:
                                System.out.println("opção invalida.");
                        }
                    }


                    break;


                 default:
                     System.out.println("opção invalida.");
            }
            menu();
            choice = Integer.parseInt(input.nextLine());
        }
    }

    private static int deleteAccount(int position, String[][] userProfiles,
                                      String[][] communities, int[][] friendships, String[][] friendsMessages, int loggedOptions) {
        System.out.println("Deseja mesmo encerrar sua conta do IFace?\n1) Sim\n2) Não");
        int choice = Integer.parseInt(input.nextLine());

        switch (choice){
            case 1:

                deleteAccountDefinitely(position, userProfiles, communities, friendships, friendsMessages);

                System.out.println("Conta encerrada ;(.");
                return -1;

            case 2:
                System.out.println("Tentativa de cancelamento de conta abortada.");

                break;
            default:
                System.out.println("Opção invalida.");
        }
        return loggedOptions;

    }

    private static void deleteAccountDefinitely(int position, String[][] userProfiles,
                                    String[][] communities, int[][] friendships, String[][] friendsMessages) {
        int i, j;

        for( i = 0; i < 200; i++){

            if(friendships[position][i] == 2){
                friendsMessages[i][position] = "";
                friendships[position][i] = 0;
                friendships[i][position] = 0;
            }
            if(communities[i][0].equals(userProfiles[position][4])){
                for(j = 0; j < 203; j++){
                    communities[i][j] = "none";
                }
            }
        }
        userProfiles[position][0] = "-";
        userProfiles[position][1] = "-";
        userProfiles[position][3] = "-";
        userProfiles[position][4] = "@";
        userProfiles[position][5] = "none";

    }

    private static void seeCommunities(int position, String[][] userProfiles, String[][] communities) {
        System.out.println("Suas comunidades são:");

        for(int i = 0; i < 200; i++){
            for(int j = 0; j < 203; j++){
                if(communities[i][j].equals(userProfiles[position][4])){
                    System.out.printf("%s\n", communities[i][1]);
                }
            }
        }
    }

    private static void seeFriends(int position, String[][] userProfiles, int[][] friendships) {
        System.out.println("Seus amigos são:");

        for(int i = 0; i < 200; i++){
            if(friendships[position][i] == 2){
                System.out.printf("%s\n", userProfiles[i][2]);
            }
        }
    }

    private static void joinCommunity(int position, String[][] userProfiles, String[][] communities) {
        int i, j;
        System.out.println("Informe o nome da comunidade que deseja ingressar:");
        String communityName = input.nextLine();


        if(userProfiles[position][5].equals(communityName)){
            System.out.println("Você já é o dono desta comunidade e já está dentro.");
            return;
        }


        for(i = 0; i < 200; i++) {
            if(communities[i][1].equals(communityName)){
                for (j = 3; j < 203; j++) {
                    if (communities[i][j].equals(userProfiles[position][4])){
                        System.out.println("Você já pertence a esta comunidade.");
                        break;
                    }

                }
                if(j >= 203){
                    for(j = 3; j < 203; j++){
                        if(communities[i][j].equals("none")){
                            communities[i][j] = userProfiles[position][4];
                            break;
                        }
                    }
                    System.out.printf("Você entrou na comunidade %s.\n", communityName);
                }
                break;
            }
        }

    }

    private static void initializeCommunities(String[][] communities) {

        for (int i = 0; i < 200; i++){
            for(int j = 0; j < 203; j++) {
                communities[i][j] = "none";
            }
        }
    }

    private static void createCommunity(int position, String[][] userProfiles, String[][] communities) {
        System.out.println("Digite o nome da comunidade que deseja criar:");
        String communityName = input.nextLine();
        int i = 0;

        while(i < 200) {
            for (i = 0; i < 200; i++) {
                if (communities[i][1].equals(communityName)) {
                    System.out.println("Uma comunidade com este nome já existe, informe outro:");
                    communityName = input.nextLine();
                    break;
                }
            }
        }
        for( i = 0; i < 200; i++){
            if(communities[i][1].equals("none")){
                communities[i][1] = communityName;
                communities[i][0] = userProfiles[position][4];

                System.out.println("Informe uma descrição para a comunidade:");
                String communityDescription = input.nextLine();

                communities[i][2] = communityDescription;

                break;
            }
        }

        userProfiles[position][5] = communityName;


        System.out.println("Comunidade criada!");
    }

    private static void showFriends(int position, int[][] friendships, String[][] userProfiles) {
        int i;
        System.out.println("Seus amigos:");
        for(i = 0; i < 200; i++){
            if(friendships[position][i] == 2){
                System.out.printf("%s\n", userProfiles[i][2]);
            }
        }
    }

    private static void initializeMessages(String[][] friendsMessages) {
        for(int i = 0; i < 200; i++){
            for(int j = 0; j < 200; j++){
                friendsMessages[i][j] = "";
            }
        }
    }

    private static void seeMessages(int position, String[][] userProfiles, String[][] friendsMessages, int[][] friendships) {
        String friendName;
        showFriends(position, friendships, userProfiles);
        System.out.println("Digite o nome do amigo que deseja ver as mensagens:");
        friendName = input.nextLine();

        for(int i = 0; i < 200; i++){
            if(friendships[i][position] == 2 && userProfiles[i][2].equals(friendName)){
                System.out.printf("%s:\n%s\n", userProfiles[i][2], friendsMessages[position][i]);
                break;
            }
        }

    }

    private static void seeProfile(int position, String[][] userProfiles) {
        System.out.printf("Nome: %s\nEndereço: %s\nEmail: %s\n", userProfiles[position][2],
                userProfiles[position][3], userProfiles[position][4]);
    }

    private static void sendMessage(int position, String[][] userProfiles, int[][] friendships, String[][] friendsMessages) {

        System.out.println("Para quem deseja enviar mensagem?");
        String friendName, message;
        int i;
        for(i = 0; i < 200; i++){
            if(friendships[position][i] == 2){
                System.out.printf("%s\n", userProfiles[i][2]);
            }
        }
        friendName = input.nextLine();

        for(i = 0; i < 200; i++){
            if(friendships[position][i] == 2 && userProfiles[i][2].equals(friendName)){
                System.out.printf("Enviando mensagem para %s...\n", userProfiles[i][2]);
                message = input.nextLine();

                friendsMessages[i][position] = friendsMessages[i][position] + "\n" + message;
                System.out.println("Mensagem enviada.");
                break;
            }
        }
    }

    private static void friendshipRequest(int position, String[][] userProfiles, int[][] friendships) {
        int i, choice;

        for(i = 0; i < 200; i++){
            if(friendships[position][i] == 1){
                System.out.printf("%s deja ser seu amigo\nDigite:\n1) aceitar\n2) recusar\n", userProfiles[i][2]);
                choice = Integer.parseInt(input.nextLine());

                switch (choice){
                    case 1:
                        friendships[position][i] = 2;
                        friendships[i][position] = 2;
                        System.out.println("Convite aceito!");
                        break;
                    case 2:
                        friendships[position][i] = 0;
                        friendships[i][position] = 0;
                        System.out.println("Convite recusado!");
                        break;
                    default:
                        System.out.println("Opção invalida!");
                       // i--;
                }
            }
        }
    }

    private static void initializeFriendships(int[][] friendships) {
        for(int i = 0; i < 200; i++){
            for(int j = 0; j < 200; j++){
                friendships[i][j] = 0;
            }
        }
    }

    private static void addFriend(int position, String[][] userProfiles, int[][] friendShips) {
        String friendEmail;

        System.out.println("Informe o email do amigo que deseja adicionar:");
        friendEmail = input.nextLine();

        int friendIndex = getFriendIndex(friendEmail, userProfiles);
        if(friendIndex == -1){
            System.out.println("Este email não pertence a nenhum membro cadastrado");
        }
        else {
            if(friendShips[friendIndex][position] == 0) {
                friendShips[friendIndex][position] = 1;
                friendShips[position][friendIndex] = 1;
                System.out.printf("Solicitação de amizade enviado para %s\n", userProfiles[friendIndex][2]);
            }
            else{
                System.out.println("Vocês já são amigos\n ou aguardando a confirmação de amizade.");
            }
        }
    }

    private static int getFriendIndex(String friendEmail, String[][]userProfiles) {

        int i;
        for(i = 0; i < 200; i++){
            if(userProfiles[i][4].equals(friendEmail)){
                return i;
            }
        }
        return -1;
    }

    private static boolean verifyLogin(String login, String[][] userProfiles, int currentSize) {
        int i;
        for (i = 0; i < currentSize; i++) {
            if (userProfiles[i][0].equals(login)) {
                System.out.println("Conta já existente\ninforme outro login:");
                return true;
            }
        }

        return false;

    }

    private static void createAccount(int position, String[][] userProfiles,
                                     String login, String name, String password) {
        userProfiles[position][0] = login;
        userProfiles[position][2] = name;
        userProfiles[position][1] = password;


    }
    private static void menu(){
        System.out.println("1) Criar conta\n2) login\n-1 para sair.");
    }

    private static void initializeProfiles(String[][] userProfiles){

        for(int i = 0; i < 200; i++){
            userProfiles[i][0] = "-";
            userProfiles[i][1] = "-";
            userProfiles[i][3] = "-";
            userProfiles[i][4] = "@";
            userProfiles[i][5] = "none";
        }
    }

    private static int getEmptyPosition(String[][] userProfiles){
        int i;
        for(i = 0; i < 200; i++){
            if(userProfiles[i][0].equals("-"))
                break;
        }
        return i;
    }

    private static int validLogin(String[][] userProfiles, String login){
        int i;
        for(i = 0; i < 200; i++){
            if(userProfiles[i][0].equals(login)){
                return i;
            }
        }
        System.out.println("Login não existe!\nInforme outro login:");
        return -1;
    }

    private static void loggedMenu(){
        System.out.println("1) Editar perfil\n2) Adicionar amigo\n3) Enviar mensagem\n4) Solicitações de amizade\n" +
                "5) Ver perfil\n6) Ver mensagens\n7) Criar comunidade\n8) Entrar em uma comunidade\n" +
                "9) Ver amigos\n10) Ver comunidades\n11) deletar conta\n-1) para sair.");
    }

    private static void editProfile(int position, String[][] userProfiles){
        System.out.println("Selecione uma das opções para preencher/editar\n" +
                "1) Senha\n2) Login\n3) Email\n4) Endereço\n5) Nome");
        int choice = Integer.parseInt(input.nextLine());
        String actual, changed = ".";
        switch (choice){
            case 1:
                System.out.println("Digite a senha atual:");
                actual = input.nextLine();


                while(!userProfiles[position][1].equals(actual)){
                    System.out.println("Senha incorreta!!\nDigite a senha correta:");
                    actual = input.nextLine();
                }
                System.out.println("Digite sua nova senha:");
                changed = input.nextLine();

                userProfiles[position][1] = changed;
                System.out.println("Senha trocada com sucesso!");
                break;
            case 2:
                System.out.println("Digite seu login atual:");
                actual = input.nextLine();


                while(!userProfiles[position][0].equals(actual)){
                    System.out.println("Login incorreto!!\nDigite o login correto:");
                    actual = input.nextLine();
                }
                System.out.println("Digite seu novo login:");

                while (true) {
                    int i;
                    changed = input.nextLine();
                    for(i = 0; i < 200; i++){
                        if(userProfiles[i][0].equals(changed)){
                            System.out.println("Login já está em uso\nescolha outro:");
                            break;
                        }
                    }
                    if(i >= 200) break;
                }
                userProfiles[position][0] = changed;
                System.out.println("Login trocado com sucesso!");
                break;
            case 3:

                System.out.println("Digite seu novo email:");


                int i = 0;
                while(i < 200) {
                    changed = input.nextLine();
                    for (i = 0; i < 200; i++) {
                        if (userProfiles[i][4].equals(changed)){
                            System.out.println("Email já registrado, escolha outro email:");
                            break;
                        }
                    }
                }


                userProfiles[position][4] = changed;
                System.out.println("Email trocado com sucesso!");
                break;
            case 4:

                System.out.println("Digite seu novo endereço:");
                changed = input.nextLine();

                userProfiles[position][3] = changed;
                System.out.println("Endereço alterado com sucesso!");
                break;
            case 5:

                System.out.println("Digite seu novo nome:");
                changed = input.nextLine();

                userProfiles[position][2] = changed;
                System.out.println("Nome alterado com sucesso!");
                break;
            case -1:
                break;
            default:
                System.out.println("opção invalida.");
        }



    }

}
