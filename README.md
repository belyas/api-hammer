
 ██████╗ ██╗    ██╗ █████╗ ███████╗ █████╗ ██████╗ 
██╔═══██╗██║    ██║██╔══██╗██╔════╝██╔══██╗██╔══██╗
██║   ██║██║ █╗ ██║███████║███████╗███████║██████╔╝
██║▄▄ ██║██║███╗██║██╔══██║╚════██║██╔══██║██╔══██╗
╚██████╔╝╚███╔███╔╝██║  ██║███████║██║  ██║██║  ██║
 ╚══▀▀═╝  ╚══╝╚══╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝
                                                   
# How to run the tests? 

1. First thing, let's build the random resources that will be used by Gatling:
    a. Run the python script `stress-test/generate_resources.py`
        - This will create the `search-terms.tsv` and `warriors-payloads.tsv`
          inside the `stress-test/user-files/resources` folder, where Gatling looks for
          payload to be used with the tests.

2. Start/build you API with docker locally.

3. After the applications is up and running:
    a. (Mac/Linux) run the `stress-test/run-test.sh`
    b. (Windows) run the `stress-test/run-test.ps1`

4. Terminal will start outputting information about the test

5. Once the test are finished, you can access the Gatling report by:
    a. Either clicking on the url provided at the end of the test
    b. Going into `./stress-test/user-files/results/englabstresstest-[timestamp]/index.html`

Good Luck! And may the force be with you!

Obs: ./deps is the folder containing the current Gatling version used for this tests

Obs: COREWAR!
