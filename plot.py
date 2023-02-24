import pandas as pd
import matplotlib.pyplot as plt

BEST_FITNESS_VALUE = "Best Fitness Value"
FITNESS_VALUE = "Fitness Value"
TOTAL_ITERATION = "Total Iteration"
ITERATION = "Iteration"

result_std = pd.read_csv("result_std.txt", sep=",", names=[TOTAL_ITERATION, BEST_FITNESS_VALUE])
result_imp = pd.read_csv("result_imp.txt", sep=",", names=[TOTAL_ITERATION, BEST_FITNESS_VALUE])

print(f"Standard GA - {TOTAL_ITERATION}: {result_std[TOTAL_ITERATION].mean()} {FITNESS_VALUE}: {result_std[BEST_FITNESS_VALUE].mean()}")
print(f"Improved GA - {TOTAL_ITERATION}: {result_imp[TOTAL_ITERATION].mean()} {FITNESS_VALUE}: {result_imp[BEST_FITNESS_VALUE].mean()}")


standard_ga_df = pd.read_csv("plot_data_std.txt", sep=",", names=[ITERATION, FITNESS_VALUE])
improved_ga_df = pd.read_csv("plot_data_imp.txt", sep=",", names=[ITERATION, FITNESS_VALUE])

figure, axis = plt.subplots(2, 1)

axis[0].plot(standard_ga_df[ITERATION], standard_ga_df[FITNESS_VALUE])
axis[0].set_title("Standard GA")
axis[0].set_xlabel(ITERATION)
axis[0].set_ylabel(FITNESS_VALUE)

axis[1].plot(improved_ga_df[ITERATION], improved_ga_df[FITNESS_VALUE])
axis[1].set_title("Improved GA")
axis[1].set_xlabel(ITERATION)
axis[1].set_ylabel(FITNESS_VALUE)

figure.tight_layout()
plt.show()
