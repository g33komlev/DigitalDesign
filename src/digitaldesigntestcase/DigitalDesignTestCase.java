package digitaldesigntestcase;

import java.util.ArrayDeque;
import java.util.Scanner;

/**
 * Digital Design test case
 *
 * @author - Gleb Komlev [g33komlev@yandex.ru]
 */
public class DigitalDesignTestCase {

  private Scanner scanner;
  private String inputString;
  private int stringLength;

  public static void main(String[] args) {
    new DigitalDesignTestCase().startProgram();
  }

  DigitalDesignTestCase() {
    scanner = new Scanner(System.in);
  }

  private void startProgram() {
    while (true) {
      System.out.println("\nВведите строку: ");
      inputString = scanner.nextLine();
      stringLength = inputString.length();

      if (isValid(inputString)) {
        System.out.println("Распакованная строка: " + unpackString(new StringBuilder(inputString), 1));
      }
    }
  }

  /**
   * Рекурсивный метод распаковки строки
   *
   * @param unpackingString - распаковываемая строка
   * @param loopCount - счетчик умножения
   *
   * @return - StringBuilder полностью распакованная строка
   */
  private StringBuilder unpackString(StringBuilder unpackingString, int loopCount) {
    // Стек открывающих скобок
    ArrayDeque<Character> openBracketStack = new ArrayDeque<>();

    // Индекс открывающей скобки и индекс соответствующей закрывающей скобки, индекс начала числа повторов распаковки
    int firstLoopCountIndex, firstOpenBracketIndex, lastClosedBracketIndex;
    firstOpenBracketIndex = lastClosedBracketIndex = firstLoopCountIndex = 0;

    // Если строка содержит распаковочные блоки
    if (unpackingString.indexOf("[") != -1) {

      // Последовательный перебор всей строки
      for (int i = 0; i < unpackingString.length(); i++) {
        String currentSymbol = String.valueOf(unpackingString.charAt(i));

        // Если текущий символ - число
        if (isNumber(currentSymbol)) {

          // Записываем индекс начала числа повторов строки, получаем число повторов строки
          firstLoopCountIndex = i;
          loopCount = Integer.parseInt(unpackingString.substring(firstLoopCountIndex, firstOpenBracketIndex = unpackingString.indexOf("[", i)));
          openBracketStack.add('[');

          // Вычисляем выражение внутри распаковочного блока
          i = firstOpenBracketIndex + 1;
          while (!openBracketStack.isEmpty()) {
            currentSymbol = String.valueOf(unpackingString.charAt(i++));

            // Если текущий символ - закрывающая скобка, то уменьшаем стек на 1
            if (isClosedBracket(currentSymbol)) {
              openBracketStack.pollLast();
              continue;
            }

            // Если текущий символ - открывающая скобка, то увеличиваем стек на 1
            if (isOpenBracket(currentSymbol)) {
              openBracketStack.add('[');
            }
          }

          // Значение (i - 1) - индекс соответствующей закрывающей скобки распаковочного блока
          lastClosedBracketIndex = i - 1;

          // Получаем подстроку с распаковочным блоком и рекурсивно отправляем его на распаковку
          String subString = unpackingString.substring(firstOpenBracketIndex + 1, lastClosedBracketIndex);
          StringBuilder receivedString = unpackString(new StringBuilder(subString), loopCount);

          // Распаковываем строку, умножая её на loopCount
          receivedString = multiplyString(receivedString, loopCount);

          // Вырезаем из искомой строки число повторов распаковки и сам блок распаковки, вставляя вместо него распакованный вариант
          unpackingString = unpackingString.replace(firstLoopCountIndex, lastClosedBracketIndex + 1, receivedString.toString());
          // Переводим цикл на шаг, соответствующий последнему символу вставленного распакованного блока
          i = firstLoopCountIndex + receivedString.length() - 1;
        }
      }

      // Возвращаем итоговую строку по выходу их цикла
      return unpackingString;
    }

    return unpackingString;
  }

  /**
   * Метод умножения строки
   *
   * @param multipliableString - размножаемая строка
   * @param loopCounter - количество умножений
   *
   * @return - размноженная StringBuilder строка
   */
  private StringBuilder multiplyString(StringBuilder multipliableString, int loopCounter) {
    StringBuilder baseString = new StringBuilder(multipliableString);
    loopCounter = (loopCounter == 0) ? 0 : loopCounter - 1;

    for (int z = 0; z < loopCounter; z++) {
      baseString.append(multipliableString);
    }

    return baseString;
  }

  /* Блок проверочных методов */
  /**
   * Метод проверки, является ли символ цифрой.
   *
   * @param inputChar - входная строка.
   *
   * @return - возвращает true, если символ является числом, иначе - false.
   */
  private boolean isNumber(String inputChar) {
    return inputChar.matches("[0-9]");
  }

  /**
   * Метод проверки, является ли символ открывающей скобкой.
   *
   * @param inputChar - входная строка.
   *
   * @return - возвращает true, если символ является открывающей скобкой, иначе - false.
   */
  private boolean isOpenBracket(String inputChar) {
    return inputChar.equals("[");
  }

  /**
   * Метод проверки, является ли символ закрывающей скобкой.
   *
   * @param inputChar - входная строка.
   *
   * @return - возвращает true, если символ является закрывающей скобкой, иначе - false.
   */
  private boolean isClosedBracket(String inputChar) {
    return inputChar.equals("]");
  }

  /**
   * Метод проверки, является ли символ буквой латинского алфавита.
   *
   * @param inputChar - входная строка.
   *
   * @return - возвращает true, если символ является буквой латинского алфавита, иначе - false.
   */
  private boolean isLetter(String inputChar) {
    return inputChar.matches("[a-zA-Z]");
  }

  /**
   * Метод валидации строки {@link DigitalDesignTestCase#inputString}. Изначально проверяет строку на правильное начало, затем поочередно проверяет каждый символ строки на соответствие условиям.
   *
   * @param stringForValidation - валидируемая строка
   *
   * @return - возвращает true, если строка валидна, иначе false.
   */
  private boolean isValid(String stringForValidation) {
    // Стек открывающих скобок. Необходим для проверки баланса скобок
    ArrayDeque<Character> openBracketStack = new ArrayDeque<>();

    // Проверяет, является ли строка пустой или же указывает на null
    if (stringForValidation == null || stringForValidation.isEmpty()) {
      System.out.println("Error: строка пуста или указывает на null");
      return false;
    }

    // Первый символ строки
    String firstSymbol = String.valueOf(stringForValidation.charAt(0));

    // Проверка первого символа: если он не является символом латинского алфавита или цифрой, возвращает false
    if (!(firstSymbol.matches("[a-zA-Z]") || firstSymbol.matches("[0-9]"))) {
      System.out.println("Error: недопустимое начало строки");
      return false;
    }

    //Поочередное сравнение каждого символа
    for (int currentIndex = 0; currentIndex < stringLength; currentIndex++) {
      // Значение текущего символа в String
      String currentSymbol = String.valueOf(stringForValidation.charAt(currentIndex));

      // Если текущий сивол - цифра
      if (isNumber(currentSymbol)) {
        // Получаем подстроку, начинающуюся с данной цифры
        String subString = stringForValidation.substring(currentIndex);

        // После текущей цифры может идти либо другая цифра, либо открывающая скобка. Объявляем индекс открывающей скобки в подстроке
        int openBracketIndex;

        // Если открывающая скобка в подстроке не найдена, метод возвращает false, иначе subString присваиваем подстроку от текущей цифры до открывающей скобки не включительно
        if ((openBracketIndex = subString.indexOf("[")) == -1) {
          System.out.println("Error: неправильный синтаксис строки!");
          return false;
        } else {
          subString = subString.substring(0, openBracketIndex);
        }

        // Проверка символов в подстроке: если в числе повторов строки есть отличные от цифр символы, метод возвращает false
        if (!subString.matches("[0-9]*")) {
          System.out.println("Error: недопустимый символ в числе повторений строки! Индекс начала числа: " + (currentIndex) + "!");
          return false;
        }

        // Переводим цикл на индекс открывающей скобки, чтобы продолжить проверку валидности с неё
        currentIndex += openBracketIndex - 1;
        continue;
      }

      // Если текущий символ - открывающая скобка
      if (isOpenBracket(currentSymbol)) {

        // Если скобка является последним символом в строке, либо перед ней не стоит цифра, либо после нее сразу установлена закрывающая скобка, либо после нее сразу установлена открывающая скобка - метод возвращает false
        if (((currentIndex + 1) == stringLength) || !isNumber(String.valueOf(stringForValidation.charAt(currentIndex - 1))) || isClosedBracket(String.valueOf(stringForValidation.charAt(currentIndex + 1))) || isOpenBracket(String.valueOf(stringForValidation.charAt(currentIndex + 1)))) {
          System.out.println("Error: недопустимое окружение открывающей скобки! Индекс скобки: " + currentIndex);
          return false;
        }

        // Добавляем скобку в стек открывающих скобок
        openBracketStack.add('[');
        continue;
      }

      // Если текущий символ - закрывающая скобка
      if (isClosedBracket(currentSymbol)) {

        // Если закрывающая скобка не является последней И после нее установлена открывающая скобка - метод возвращает false
        if (!((currentIndex + 1) == stringLength) && isOpenBracket(String.valueOf(currentIndex + 1))) {
          System.out.println("Error: недопустимое окружение закрывающей скобки! Индекс скобки: " + currentIndex);
          return false;
        }

        // Проверка, является ли стек открывающих скобок пустым. Если да - метод возвращает false
        if (openBracketStack.pollLast() == null) {
          System.out.println("Error: неверный баланс скобок");
          return false;
        }

        continue;
      }

      // Если текущий символ - не буква латинского алфавита
      if (!isLetter(currentSymbol)) {
        System.out.println("Error: недопустимый символ по индексу: " + currentIndex);
        return false;
      }
    }

    // Итоговая проверка баланса скобок, если остались открывающие скобки - метод возвращает false
    if (!openBracketStack.isEmpty()) {
      System.out.println("Error: неверный баланс скобок");
      return false;
    }

    // Если все условия успешно пройдены - метод возвращает true
    System.out.println("\nСтрока валидна!");
    return true;
  }

}
