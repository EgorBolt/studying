import csv

with open('stage3_test.csv', mode='r') as csv_in:
    with open('images_more_than_3.csv', 'a') as csv_out1:
        with open('prices.csv', 'a') as csv_out2:
            with open('without_columns.csv', 'a') as csv_out3:
                reader = csv.reader(csv_in)
                writer_task1 = csv.writer(csv_out1, delimiter=",")
                writer_task2 = csv.writer(csv_out2, delimiter=",")
                writer_task3 = csv.writer(csv_out3, delimiter=",")
                for row in range(1):
                    next(reader)
                for row in reader:
                    if row[1].count(",") >= 3:
                        writer_task1.writerow(row)
                    if 10000 < float(row[4]) < 50000:
                        writer_task2.writerow(row)
                    new_row = [row[0], row[2], row[4]]
                    writer_task3.writerow(new_row)


