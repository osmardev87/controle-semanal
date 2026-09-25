# Guia do painel administrativo

Este guia explica como configurar o primeiro administrador e usar o painel para gerenciar contas do Meu Bolso.

## O que fica salvo

- O usuário e a senha do administrador são salvos na tabela `admin_accounts` do banco. A senha é armazenada como hash BCrypt.
- O `.env` continua sendo usado para as configurações de conexão com o banco (`DB_URL`, `DB_USERNAME` e `DB_PASSWORD`). Ele não precisa conter usuário, senha ou token administrativo.
- O primeiro administrador é uma conta única. Depois da configuração inicial, o painel exige login.

## Iniciar localmente

Na raiz do projeto, confirme que o `.env` contém as três variáveis de conexão com o banco e inicie a aplicação:

```powershell
.\mvnw.cmd spring-boot:run
```

Abra o painel em:

```text
http://localhost:8080/admin.html
```

## Iniciar com Docker Compose

Na raiz do projeto, confirme que o `.env` contém `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`. Depois construa e inicie a aplicação:

```powershell
docker compose up -d --build
```

Abra o painel em `http://localhost:8080/admin.html` ou use o endereço HTTPS do servidor quando estiver publicado.

## Configurar o primeiro administrador

Quando ainda não existir uma conta administrativa no banco, a aplicação gera um código de configuração de uso único e o escreve no log de inicialização. No Docker, localize o código com:

```powershell
docker compose logs controle-semanal | Select-String "ADMIN SETUP ONE-TIME CODE"
```

Copie o código mais recente para o formulário de configuração inicial em `admin.html`, informe um nome de usuário e crie uma senha administrativa com pelo menos 6 caracteres.

O código é invalidado assim que a conta é criada. Enquanto a configuração inicial não for concluída, cada reinicialização gera um código novo; nesse caso, use o código do log da inicialização mais recente. Mantenha os logs protegidos e não compartilhe o código.

## Gerenciar contas

Depois de entrar no painel, você pode:

- **Criar uma conta:** informe nome, telefone e senha temporária de pelo menos 6 caracteres. A conta exigirá troca de senha no primeiro acesso.
- **Editar uma conta:** altere o nome e o telefone pelos controles da lista.
- **Redefinir senha:** gere uma nova senha temporária. A pessoa deverá trocá-la ao entrar novamente.
- **Verificar o estado da senha:** a lista indica quando uma conta ainda não tem senha, aguarda troca ou já definiu uma senha pessoal.

Entregue senhas temporárias diretamente à pessoa por um canal seguro. Não as coloque neste arquivo, no `.env` ou em mensagens públicas.

## Primeiro acesso do usuário

A pessoa acessa a aplicação principal, entra com o telefone e a senha temporária e, quando solicitado, define uma senha pessoal com pelo menos 6 caracteres.

## Encerrar a sessão administrativa

Use o botão **Sair** no painel. As sessões expiram após 12 horas de inatividade conforme a configuração do servidor.

## Segurança ao publicar

- Use HTTPS para acessar o painel fora do computador local.
- Restrinja o acesso aos logs do servidor, especialmente antes de criar o primeiro administrador.
- O caminho `admin.html` não é uma barreira de segurança. A API exige autenticação administrativa para listar ou alterar contas.
- Não adicione credenciais administrativas ao Git ou ao `.env`.