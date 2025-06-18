from datetime import datetime
import csv
from collections import defaultdict
import matplotlib.pyplot as plt

class Car:
    def __init__(self, make, model, year, price, quantity):
        self.make = make
        self.model = model
        self.year = year
        self.price = price
        self.quantity = quantity

class Employee:
    def __init__(self, employee_id, name, username, password):
        self.employee_id = employee_id
        self.name = name
        self.username = username
        self.password = password

class Customer:
    def __init__(self, name, username, password):
        self.name = name
        self.username = username
        self.password = password

class Appointment:
    def __init__(self, customer_username, car_make, car_model, appointment_date):
        self.customer_username = customer_username
        self.car_make = car_make
        self.car_model = car_model
        self.appointment_date = appointment_date

class Dealership:
    def __init__(self):
        self.employees = []
        self.customers = []
        self.inventory = []
        self.appointments = []
        self.expenses = []
        self.sales_data = defaultdict(float)
        self.expenses_data = defaultdict(float)

    def load_employees(self, filename):
        with open(filename, 'r') as file:
            reader = csv.reader(file)
            for row in reader:
                employee = Employee(row[0], row[1], row[2], row[3])
                self.employees.append(employee)

    def authenticate_employee(self, username, password):
        for employee in self.employees:
            if employee.username == username and employee.password == password:
                return employee
        return None

    def load_customers(self, filename):
        with open(filename, 'r') as file:
            reader = csv.reader(file)
            for row in reader:
                customer = Customer(row[0], row[1], row[2])
                self.customers.append(customer)

    def authenticate_customer(self, username, password):
        for customer in self.customers:
            if customer.username == username and customer.password == password:
                return customer
        return None

    def schedule_appointment(self, customer_username, car_make, car_model, appointment_date):
        appointment = Appointment(customer_username, car_make, car_model, appointment_date)
        self.appointments.append(appointment)

        # Write appointment data to CSV file
        with open('appointments.csv', 'a', newline='') as file:
            writer = csv.writer(file)
            writer.writerow([appointment.customer_username, appointment.car_make, appointment.car_model, appointment.appointment_date.strftime("%Y-%m-%d %H:%M:%S")])

    def display_appointments_for_customer(self, customer_username):
        print("Your Appointments:")
        for appointment in self.appointments:
            if appointment.customer_username == customer_username:
                print(f"Car: {appointment.car_make} {appointment.car_model} | Date: {appointment.appointment_date}")

    def load_inventory(self, filename):
        with open(filename, 'r') as file:
            reader = csv.reader(file)
            for row in reader:
                car = Car(row[0], row[1], int(row[2]), float(row[3]), int(row[4]))
                self.inventory.append(car)

    def update_inventory_file(self, filename):
        with open(filename, 'w', newline='') as file:
            writer = csv.writer(file)
            for car in self.inventory:
                writer.writerow([car.make, car.model, car.year, car.price, car.quantity])

    def view_inventory(self):
        print("Available cars in inventory:")
        for car in self.inventory:
            print(f"{car.year} {car.make} {car.model}, Price: ${car.price}, Quantity: {car.quantity}")

    def load_appointments_from_file(self, filename):
        self.appointments = []
        try:
            with open(filename, 'r') as file:
                reader = csv.reader(file)
                for row in reader:
                    appointment = Appointment(row[0], row[1], row[2], datetime.strptime(row[3], "%Y-%m-%d %H:%M:%S"))
                    self.appointments.append(appointment)
            print("Appointments loaded successfully.")
        except FileNotFoundError:
            print("Appointments file not found.")
        except Exception as e:
            print("Error loading appointments:", e)

    def display_all_appointments(self):
        print("All Appointments:")
        if not self.appointments:
            print("No appointments found.")
        else:
            for appointment in self.appointments:
                print(f"Customer: {appointment.customer_username}, Car: {appointment.car_make} {appointment.car_model}, Date: {appointment.appointment_date}")

    def load_expenses(self, filename):
        with open(filename, 'r') as file:
            reader = csv.reader(file)
            for row in reader:
                transaction_type, amount, employee_id, date = row
                if transaction_type == 'Sale':
                    self.sales_data[employee_id] += float(amount)
                elif transaction_type == 'Expense':
                    self.expenses_data[employee_id] += float(amount)
                self.expenses.append({"type": transaction_type, "amount": float(amount), "employee_id": employee_id, "date": datetime.strptime(date, "%Y-%m-%d %H:%M:%S")})
   
    def record_sale(self, employee_id, amount):
        # Placeholder logic to record sale
        print(f"Sale recorded by Employee ID {employee_id} for amount ${amount}")
        # Write sale record to CSV file
        with open('expenses.csv', 'a', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(['Sale', amount, employee_id, datetime.now().strftime("%Y-%m-%d %H:%M:%S")])

    def record_expense(self, employee_id, amount):
        # Placeholder logic to record expense
        print(f"Expense recorded by Employee ID {employee_id} for amount ${amount}")
        # Write expense record to CSV file
        with open('expenses.csv', 'a', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(['Expense', amount, employee_id, datetime.now().strftime("%Y-%m-%d %H:%M:%S")])

    def generate_sales_report_per_employee(self):
        # Generate and display sales report per employee
        print("\nGenerating Sales Report per Employee...")
        print("Sales Report per Employee:")
        for employee_id, amount in self.sales_data.items():
            print(f"Employee ID: {employee_id}, Total Sales: ${amount}")

        # Generate and display sales report graph per employee
        plt.bar(self.sales_data.keys(), self.sales_data.values(), color='skyblue')
        plt.title('Sales Report per Employee')
        plt.xlabel('Employee ID')
        plt.ylabel('Total Sales ($)')
        plt.show()

    def generate_expenses_report_per_employee(self):
        # Generate and display expenses report per employee
        print("\nGenerating Expenses Report per Employee...")
        print("Expenses Report per Employee:")
        for employee_id, amount in self.expenses_data.items():
            print(f"Employee ID: {employee_id}, Total Expenses: ${amount}")

        # Generate and display expenses report graph per employee
        plt.bar(self.expenses_data.keys(), self.expenses_data.values(), color='lightcoral')
        plt.title('Expenses Report per Employee')
        plt.xlabel('Employee ID')
        plt.ylabel('Total Expenses ($)')
        plt.show()

    def generate_financial_report(self):
        # Read data from expenses.csv file
        sales_data = defaultdict(float)
        expenses_data = defaultdict(float)
        with open('expenses.csv', 'r') as file:
            reader = csv.reader(file)
            for row in reader:
                transaction_type = row[0]
                amount = float(row[1])
                employee_id = row[2]
                date = datetime.strptime(row[3], "%Y-%m-%d %H:%M:%S")

                # Process the transaction type
                if transaction_type == 'Sale':
                    sales_data[(employee_id, date.month)] += amount
                elif transaction_type == 'Expense':
                    expenses_data[(employee_id, date.month)] += amount

        # Generate financial report
        print("\nGenerating financial report...")
        print("Financial report per Employee:")
        employees = set([key[0] for key in sales_data.keys()] + [key[0] for key in expenses_data.keys()])
        for employee_id in employees:
            total_sales = sum([sales_data[(employee_id, month)] for month in range(1, 13)])
            total_expenses = sum([expenses_data[(employee_id, month)] for month in range(1, 13)])
            total_profit = total_sales - total_expenses
            print(f"\nEmployee ID: {employee_id}")
            print(f"Total Sales: ${total_sales}")
            print(f"Total Expenses: ${total_expenses}")
            print(f"Total Profit: ${total_profit}")

        # Generate and display financial report graph
        months = range(1, 13)
        for employee_id in employees:
            sales = [sales_data[(employee_id, month)] for month in months]
            expenses = [expenses_data[(employee_id, month)] for month in months]
            net_profit = [sales_data[(employee_id, month)] - expenses_data[(employee_id, month)] for month in months]
            plt.plot(months, sales, label=f'Sales (Employee {employee_id})')
            plt.plot(months, expenses, label=f'Expenses (Employee {employee_id})')
            plt.plot(months, net_profit, label=f'Net Profit (Employee {employee_id})')
        plt.title('Financial Report per Employee')
        plt.xlabel('Month')
        plt.ylabel('Amount ($)')
        plt.legend()
        plt.show()

    def buy_car(self, customer_username, car_make, car_model):
        for car in self.inventory:
            if car.make == car_make and car.model == car_model and car.quantity > 0:
                print(f"Congratulations! You have purchased a {car.year} {car.make} {car.model} for ${car.price}.")
                self.appointments.append(Appointment(customer_username, car.make, car.model, datetime.now()))
                car.quantity -= 1
                self.update_inventory_file("inventory.csv")
                return
        print("Sorry, the requested car is not available in the inventory or out of stock.")


def run_demo_application(dealership):
    dealership.load_employees("employees.csv")
    dealership.load_customers("customers.csv")
    dealership.load_inventory("inventory.csv")
    dealership.load_expenses("expenses.csv")
    dealership.load_appointments_from_file("appointments.csv")
    # Sample usage of each functionality
    print("\n\nDemo Application:")
    
    # 1. Displaying inventory
    print("\n1. Displaying inventory:")
    dealership.view_inventory()
    
    # 2. Scheduling appointment
    print("\n2. Scheduling appointment:")
    dealership.schedule_appointment("Smit_Mistry", "Toyota", "Camry", datetime.now())

    # 3. Viewing appointments
    print("\n3. Viewing appointments:")
    dealership.display_appointments_for_customer("Smit_Mistry")

    # 4. Buying a car
    print("\n4. Buying a car:")
    dealership.buy_car("Smit_Mistry", "Toyota", "Corolla")

    # 5. Recording a sale
    print("\n5. Recording a sale:")
    dealership.record_sale("prit001", 25000.0)

    # 6. Recording an expense
    print("\n6. Recording an expense:")
    dealership.record_expense("prit001", 5000.0)

    # 7. Generating a financial report
    print("\n7. Generating a financial report:")
    dealership.generate_financial_report()

    # 8. Generating a sales report per employee
    print("\n8. Generating a sales report per employee:")
    dealership.generate_sales_report_per_employee()

    # 9. Generating an expenses report per employee
    print("\n9. Generating an expenses report per employee:")
    dealership.generate_expenses_report_per_employee()

def run_access_application():
    dealership = Dealership()

    # Load employees, customers, and inventory from files
    dealership.load_employees("employees.csv")
    dealership.load_customers("customers.csv")
    dealership.load_inventory("inventory.csv")
    dealership.load_expenses("expenses.csv")
    dealership.load_appointments_from_file("appointments.csv")

    print("Welcome to the Car Dealership Access Application!")

    while True:
        print("\nPlease select an option:")
        print("1) Customer login")
        print("2) Employee login")
        print("3) Exit")

        choice = input("Enter your choice: ")

        if choice == "1":
            username = input("Enter your username: ")
            password = input("Enter your password: ")

            customer = dealership.authenticate_customer(username, password)
            if customer:
                print(f"Welcome, {customer.name}!")
                while True:
                    print("\nCustomer Menu:")
                    print("1) Schedule Appointment")
                    print("2) View Appointments")
                    print("3) View Inventory")
                    print("4) Buy a Car")
                    print("5) Exit")
                    customer_choice = input("Enter your choice: ")

                    if customer_choice == "1":
                        make = input("Enter car make: ")
                        model = input("Enter car model: ")
                        date_time = input("Enter appointment date and time (YYYY-MM-DD HH:MM): ")
                        appointment_date = datetime.strptime(date_time, "%Y-%m-%d %H:%M")
                        dealership.schedule_appointment(customer.username, make, model, appointment_date)
                    elif customer_choice == "2":
                        dealership.display_appointments_for_customer(customer.username)
                    elif customer_choice == "3":
                        dealership.view_inventory()
                    elif customer_choice == "4":
                        make = input("Enter car make: ")
                        model = input("Enter car model: ")
                        dealership.buy_car(customer.username, make, model)
                    elif customer_choice == "5":
                        break
                    else:
                        print("Invalid choice. Please enter a valid option.")
            else:
                print("Invalid username or password.")
                
        elif choice == "2":
            username = input("Enter your username: ")
            password = input("Enter your password: ")

            employee = dealership.authenticate_employee(username, password)
            if employee:
                print(f"Welcome, {employee.name}!")
                while True:
                    print("\nEmployee Menu:")
                    print("1) Record Sale")
                    print("2) Record Expense")
                    print("3) Generate Financial Report")
                    print("4) Generate Sales Report per Employee")
                    print("5) Generate Expenses Report per Employee")
                    print("6) List Employees")
                    print("7) List Customers")
                    print("8) View All Appointments")
                    print("9) Exit")
                    employee_choice = input("Enter your choice: ")

                    if employee_choice == "1":
                        amount = float(input("Enter sale amount: "))
                        dealership.record_sale(employee.employee_id, amount)
                    elif employee_choice == "2":
                        amount = float(input("Enter expense amount: "))
                        dealership.record_expense(employee.employee_id, amount)
                    elif employee_choice == "3":
                        dealership.generate_financial_report()
                    elif employee_choice == "4":
                        dealership.generate_sales_report_per_employee()
                    elif employee_choice == "5":
                        dealership.generate_expenses_report_per_employee()
                    elif employee_choice == "6":
                        print("\nList of Employees:")
                        for emp in dealership.employees:
                            print(f"ID: {emp.employee_id}, Name: {emp.name}")
                    elif employee_choice == "7":
                        print("\nList of Customers:")
                        for cust in dealership.customers:
                            print(f"Name: {cust.name}, Username: {cust.username}")
                    elif employee_choice == "8":
                        dealership.display_all_appointments()
                    elif employee_choice == "9":
                        break
                    else:
                        print("Invalid choice. Please enter a valid option.")
            else:
                print("Invalid username or password.")
                
        elif choice == "3":
            print("Exiting...")
            break
        else:
            print("Invalid choice. Please enter a valid option.")

def main():
    print("\nWelcome to the Car Dealership Application!")
    print("\nPlease select an option:")
    print("1) Demo application")
    print("2) Access application")
    
    choice = input("\nEnter your choice: ")

    if choice == "1":
        dealership = Dealership()
        run_demo_application(dealership)
    elif choice == "2":
        run_access_application()
    else:
        print("Invalid choice. Please select 1 or 2.")

if __name__ == "__main__":
    main()
