import pandas as pd
import matplotlib.pyplot as plt

BEST_FITNESS_VALUE = "Best Fitness Value"
FITNESS_VALUE = "Fitness Value"
TOTAL_ITERATION = "Total Iteration"
ITERATION = "Iteration"
TIME = "Time"

result_std = pd.read_csv("result_std.txt", sep=",", names=[TOTAL_ITERATION, BEST_FITNESS_VALUE])
result_imp = pd.read_csv("result_imp.txt", sep=",", names=[TOTAL_ITERATION, BEST_FITNESS_VALUE])

print(f"Standard GA - {TOTAL_ITERATION}: {result_std[TOTAL_ITERATION].mean()} {FITNESS_VALUE}: {result_std[BEST_FITNESS_VALUE].mean()}")
print(f"Improved GA - {TOTAL_ITERATION}: {result_imp[TOTAL_ITERATION].mean()} {FITNESS_VALUE}: {result_imp[BEST_FITNESS_VALUE].mean()}")


standard_ga_iteration_df = pd.read_csv("plot_data_std.txt", sep=",", names=[ITERATION, FITNESS_VALUE])
improved_ga_iteration_df = pd.read_csv("plot_data_imp.txt", sep=",", names=[ITERATION, FITNESS_VALUE])

standard_ga_time_df = pd.read_csv("time_based_plot_data_std.txt", sep=",", names=[TIME, FITNESS_VALUE])
improved_ga_time_df = pd.read_csv("time_based_plot_data_imp.txt", sep=",", names=[TIME, FITNESS_VALUE])

figure, axis = plt.subplots(2, 1)
figure.tight_layout(pad=3.5)

axis[0].plot(standard_ga_iteration_df[ITERATION], standard_ga_iteration_df[FITNESS_VALUE], label="Standard GA")
axis[0].plot(improved_ga_iteration_df[ITERATION], improved_ga_iteration_df[FITNESS_VALUE], label="Improved GA")
axis[0].set_xlabel(ITERATION)
axis[0].set_ylabel(FITNESS_VALUE)
axis[0].set_title("Comparison by iteration")
axis[0].legend()

axis[1].plot(standard_ga_time_df[TIME], standard_ga_time_df[FITNESS_VALUE], label="Standard GA")
axis[1].plot(improved_ga_time_df[TIME], improved_ga_time_df[FITNESS_VALUE], label="Improved GA")
axis[1].set_xlabel(TIME)
axis[1].set_ylabel(FITNESS_VALUE)
axis[1].set_title("Comparison by iteration")
axis[1].legend()

plt.show()
