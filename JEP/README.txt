1. First of all, you need to configure VM options (e.g. IntelliJ) to reach the python path (3.12 preferred):
    > -Djava.library.path=/opt/homebrew/lib/python3.12/site-packages/jep

2. Install JEP pip package:
    > python3.12 -m pip install --break-system-packages jep

3. Install other necessary pip packages using the same command as 2. For example, to run this project you need to:
    > python3.12 -m pip install --break-system-packages numpy