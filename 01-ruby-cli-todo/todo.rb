# encoding: UTF-8

STDIN.set_encoding(Encoding::UTF_8)
STDOUT.set_encoding(Encoding::UTF_8)

class TodoList
  def initialize
    @todos = []
    @next_id = 1
  end

  def add(title)
    title = title.strip
    return nil if title.empty?

    todo = { id: @next_id, title: title, done: false }
    @todos << todo
    @next_id += 1
    todo
  end

  def complete(id)
    todo = @todos.find { |item| item[:id] == id }
    return nil unless todo

    todo[:done] = true
    todo
  end

  def all
    @todos
  end
end

def print_todos(todos)
  if todos.empty?
    puts "Todoはありません。"
    return
  end

  todos.each do |todo|
    mark = todo[:done] ? "x" : " "
    puts "#{todo[:id]}. [#{mark}] #{todo[:title]}"
  end
end

list = TodoList.new

puts "CLI Todo"
puts "commands: add <title> | list | done <id> | help | exit"

loop do
  print "> "
  input = gets
  break if input.nil?

  command, argument = input.strip.split(/\s+/, 2)

  case command
  when "add"
    todo = list.add(argument.to_s)
    if todo
      puts "追加しました: #{todo[:id]}. #{todo[:title]}"
    else
      puts "タイトルを入力してください。"
    end
  when "list"
    print_todos(list.all)
  when "done"
    id = Integer(argument, exception: false)
    todo = id && list.complete(id)
    if todo
      puts "完了しました: #{todo[:id]}. #{todo[:title]}"
    else
      puts "存在するTodoのIDを指定してください。"
    end
  when "help"
    puts "add <title> : Todoを追加"
    puts "list        : Todoを一覧表示"
    puts "done <id>   : Todoを完了"
    puts "exit        : 終了"
  when "exit", "quit"
    break
  when nil, ""
    next
  else
    puts "不明なコマンドです。help を入力してください。"
  end
end

puts "終了します。Todoはメモリ上だけにあるため保存されません。"
