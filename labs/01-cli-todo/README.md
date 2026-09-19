# 01 CLI Todo

## Learning objective

Rubyの小さなCLIを通して、標準入力からコマンドを受け取る流れ、プロセス内のメモリに状態を持つこと、入力を解析して処理を分岐する方法、プロセス終了時に状態が失われることを理解する。

## Run

```bash
ruby todo.rb
```

Commands:

```text
add <title>
list
done <id>
help
exit
```

## How it works

`TodoList` が `@todos` という配列を持ち、TodoをHashとして保持する。

```text
keyboard -> STDIN (gets) -> command / argument に分割 -> case -> TodoList -> STDOUT (puts)
```

`add` するとRubyプロセスが使用しているメモリ上の配列にTodoが追加される。`list` と `done` も同じ配列を参照するため、プログラムを実行している間は状態が残る。

しかしファイルやデータベースには何も書いていない。`exit` でRubyプロセスが終了すると、そのプロセスが持っていたメモリ上の状態も使えなくなる。そのため、もう一度起動するとTodoは空になる。

## Failure experiments

1. `add` だけ入力すると空タイトルを拒否する。
2. `done abc` や存在しないIDでもクラッシュしない。
3. 未知のコマンドでは `help` を案内する。
4. Todoを追加して終了し、再起動して `list` すると空になる。

## Why this design

永続化やフレームワークを入れず、CLIの入力、処理、メモリ上の状態という最小限の仕組みだけを見えるようにした。`TodoList` に状態操作をまとめ、CLI部分は入力を受け取って操作を選ぶことに集中させている。

## Weaknesses / production differences

終了するとTodoが消え、複数プロセスで共有できない。編集・削除、自動テスト、厳密な入力解析もない。実用アプリならファイルやDBへの永続化、テスト、ログ、入力検証などを追加する。

## Questions

1. `@todos` に追加したTodoが、同じ実行中の `list` から見えるのはなぜ？
2. Rubyを終了してもう一度起動するとTodoが消えるのはなぜ？
3. 再起動後も残すには、どこに状態を書き出せばよい？
