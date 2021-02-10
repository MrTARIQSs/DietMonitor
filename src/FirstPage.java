import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FirstPage extends Application {

	// start of inst variables

	PrintWriter pw;

	Meal[] meals = new Meal[3];

	int mealNumber = 1;

	int days;
	int day;
	int month;
	int year;

	Label selected = new Label("selected food:");
	Label toSelect = new Label("food to select:");

	ListView<Edible> selection = new ListView();

	TextField searchField = new TextField();
	Button search = new Button("Search");
	Label filter = new Label("filters:");
	final ToggleGroup group = new ToggleGroup();
	RadioButton food = new RadioButton("Food only");
	RadioButton beverage = new RadioButton("Beverages only");
	RadioButton both = new RadioButton("Both");
	ListView<Edible> list = new ListView();

	Button start = new Button("Start");
	Button progress = new Button("Show your progress");

	Button nextMeal = new Button("Pick Breakfast");
	Button clearAll = new Button("Clear selection");

	ArrayList<Edible> edibles = new ArrayList<Edible>(); // to show both in case both are ticked
	ArrayList<Beverage> beverages = new ArrayList<Beverage>(); // to show list of beverage in case only it is ticked
	ArrayList<Food> foods = new ArrayList<Food>(); // to show list of food in case only food is ticked

	// end of inst variables

	public void makeFile(int num) throws FileNotFoundException {
		pw = new PrintWriter(new FileOutputStream("edibleSaveData.txt", true));
		try {
			if (meals[num].getMeals().isEmpty())
				pw.println(0.0 + "\n" + 0.0 + " " + "g" + "\n" + 0.0 + "\n" + 0.0 + "\n" + 0.0 + "\n" + 0.0);
			else {
				for (int i = 0; i < meals[num].getMeals().get(days).size(); i++)
					pw.println(meals[num].getMeals().get(days).get(i).getName() + "\n"
							+ meals[num].getMeals().get(days).get(i).getPortionSize() + " "
							+ meals[num].getMeals().get(days).get(i).getUnit() + "\n"
							+ meals[num].getMeals().get(days).get(i).getCal() + "\n"
							+ meals[num].getMeals().get(days).get(i).getFat() + "\n"
							+ meals[num].getMeals().get(days).get(i).getCarb() + "\n"
							+ meals[num].getMeals().get(days).get(i).getProtein());
			}
		} catch (NullPointerException e) {
			System.out.println("meal skipped");
		}
		pw.close();
	}

	public void getCurrentDate() {
		String data = "";
		try {
			Scanner scn = new Scanner(new FileInputStream("edibleSaveData.txt"));
			if (scn.hasNextLine()) {
				while (scn.hasNextLine()) {
					data = scn.nextLine();
					if (data.contains("date")) {
						day = Integer.parseInt(data.substring(6, data.indexOf("/")));
						month = Integer.parseInt(data.substring(data.indexOf("/") + 1, data.lastIndexOf("/")));
						year = Integer.parseInt(data.substring(data.lastIndexOf("/") + 1));
						if (month > 1) {
							days = day + 31 * (month - 1);
							if (year > 2019)
								days = day + 31 * 12 * (year - 2019);
						}
					}
				}
				day++;
				days++;
			} else {
				days = 01;
				day = 01;
				month = 04;
				year = 2019;
			}
			scn.close();
		} catch (FileNotFoundException e) {
		}
	}

	// start of reading method
	public void readList() {
		try {
			Scanner input = new Scanner(new FileInputStream("nutritionvalues-data.csv"));
			boolean stop = false; // to stop the loop
			String names = "";
			String[] cells;

			// reading starts here ;)
			input.nextLine();
			input.nextLine();
			try {
				while (input.hasNext() && !stop) {

					String line = input.nextLine();
					if (line.contains("\"")) {
						names = line.substring(1, line.lastIndexOf("\""));
						cells = line.substring(line.lastIndexOf("\"") + 2).split(",");
					} else {
						names = line.substring(0, line.indexOf(","));
						cells = line.substring(line.indexOf(",") + 1).split(",");
					}
					double portionSizes = Double.parseDouble(cells[0]);
					String units = cells[1];
					double cals = Double.parseDouble(cells[2]);
					double fats = Double.parseDouble(cells[3]);
					double proteins = Double.parseDouble(cells[4]);
					double carbs = Double.parseDouble(cells[5]);

					if (units.equalsIgnoreCase("ml")) {
						edibles.add(new Beverage(names, portionSizes, units, cals, fats, proteins, carbs));
						beverages.add(new Beverage(names, portionSizes, units, cals, fats, proteins, carbs));
					} else if (units.equalsIgnoreCase("g")) {
						edibles.add(new Food(names, portionSizes, units, cals, fats, proteins, carbs));
						foods.add(new Food(names, portionSizes, units, cals, fats, proteins, carbs));
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				stop = true;
			}
			// reading ends here ;)
		} catch (FileNotFoundException e) {
		}
	}
	// end of reading method

	// start of gui launch
	public static void main(String[] args) {
		Application.launch(args);
	}
	// end of gui launch

	// start of gui
	public void start(Stage stage) {
		meals[0] = new Meal();
		meals[1] = new Meal();
		meals[2] = new Meal();
		readList();
		getCurrentDate();
		VBox root = new VBox();
		Image icon = new Image("File:image.png");
		Image v2030 = new Image("File:Vision2030-1.png");
		root.setPrefWidth(100);

		ImageView image = new ImageView();
		image.setFitHeight(90);
		image.setFitWidth(140);
		image.setImage(v2030);
		root.getChildren().addAll(image);

		stage.getIcons().add(icon);
		root.getChildren().add(start);
		root.getChildren().add(progress);
		Scene scene = new Scene(root, 500, 500);
		stage.setScene(scene);
		stage.show();
		start.setMinWidth(root.getPrefWidth());
		progress.setMinWidth(root.getPrefWidth());

		start.setPrefHeight(200);
		start.setPrefWidth(300);

		progress.setPrefHeight(200);
		progress.setPrefWidth(300);

		root.setAlignment(Pos.TOP_CENTER);
		root.setAlignment(Pos.BOTTOM_CENTER);

		start.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				VBox secondaryLayout = new VBox();
				Button back = new Button("Back");

				both.setToggleGroup(group);
				food.setToggleGroup(group);
				beverage.setToggleGroup(group);

				Label date = new Label("Date: " + day + "/" + month + "/" + year);

				ComboBox<String> sortMenu = new ComboBox<String>();
				sortMenu.getItems().addAll("sort by name (ascending)", "sort by name (decending)",
						"sort by calories (ascending)", "sort by calories (decending)", "sort by proteins (ascending)",
						"sort by proteins (decending)");
				sortMenu.setPromptText("sort by:");
				sortMenu.setMaxSize(200, 10);

				HBox row1 = new HBox();
				HBox row2 = new HBox();
				HBox row3 = new HBox();
				HBox row4 = new HBox();
				HBox row5 = new HBox();
				HBox row6 = new HBox();
				row6.setSpacing(40);
				row4.setSpacing(175);
				row1.getChildren().add(searchField);
				row1.getChildren().add(search);
				secondaryLayout.getChildren().add(row1);
				secondaryLayout.getChildren().add(filter);
				row2.getChildren().add(beverage);
				row2.getChildren().add(food);
				row2.getChildren().add(both);
				secondaryLayout.getChildren().add(row2);
				row3.getChildren().add(sortMenu);
				row3.setSpacing(60);
				row3.getChildren().add(back);
				secondaryLayout.getChildren().add(row3);
				row4.getChildren().add(toSelect);
				row4.getChildren().add(selected);
				secondaryLayout.getChildren().add(row4);
				row5.getChildren().add(list);
				row5.getChildren().add(selection);
				secondaryLayout.getChildren().add(row5);
				row6.getChildren().add(date);
				row6.getChildren().add(nextMeal);
				row6.getChildren().add(clearAll);
				secondaryLayout.getChildren().add(row6);
				Scene scene2 = new Scene(secondaryLayout, 500, 500);
				// *********CHANGED*********

				Button btn = (Button) event.getSource();
				Stage stage = (Stage) btn.getScene().getWindow();
				stage.setScene(scene2);
				stage.show();
				stage.setTitle("meal menu");

				back.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						stage.setScene(scene);
					}
				});
				group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

					public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue,
							Toggle newValue) {
						if (group.getSelectedToggle() != null) {

							if (food.isSelected()) {
								list.getItems().clear();
								for (Food a : foods) {
									list.getItems().add(a);
								}

								search.setOnAction(new EventHandler<ActionEvent>() {

									public void handle(ActionEvent event) {
										if (searchField.getText().equals(null)) {

										} else {
											list.getItems().clear();
											for (Food a : foods) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}

									}
								});

								sortMenu.getSelectionModel().selectedItemProperty().addListener((options, ov, nv) -> {
									if (nv.equals("sort by name (decending)")) {
										Collections.sort(foods, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getName().charAt(0) > rhs.getName().charAt(0) ? -1
														: (lhs.getName().charAt(0) < rhs.getName().charAt(0)) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Food a : foods) {
												list.getItems().add(a);
											}
										} else {
											for (Food a : foods) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by calories (decending)")) {
										Collections.sort(foods, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getCal() > rhs.getCal() ? -1
														: (lhs.getCal() < rhs.getCal()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Food a : foods) {
												list.getItems().add(a);
											}
										} else {
											for (Food a : foods) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by proteins (decending)")) {
										Collections.sort(foods, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getProtein() > rhs.getProtein() ? -1
														: (lhs.getProtein() < rhs.getProtein()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Food a : foods) {
												list.getItems().add(a);
											}
										} else {
											for (Food a : foods) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by name (ascending)")) {
										Collections.sort(foods, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getName().charAt(0) < rhs.getName().charAt(0) ? -1
														: (lhs.getName().charAt(0) > rhs.getName().charAt(0)) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Food a : foods) {
												list.getItems().add(a);
											}
										} else {
											for (Food a : foods) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by calories (ascending)")) {
										Collections.sort(foods, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getCal() < rhs.getCal() ? -1
														: (lhs.getCal() > rhs.getCal()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Food a : foods) {
												list.getItems().add(a);
											}
										} else {
											for (Food a : foods) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by proteins (ascending)")) {
										Collections.sort(foods, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getProtein() < rhs.getProtein() ? -1
														: (lhs.getProtein() > rhs.getProtein()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Food a : foods) {
												list.getItems().add(a);
											}
										} else {
											for (Food a : foods) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									}
								});

							} else if (beverage.isSelected()) {
								list.getItems().clear();
								for (Beverage a : beverages) {
									list.getItems().add(a);
								}

								search.setOnAction(new EventHandler<ActionEvent>() {

									public void handle(ActionEvent event) {
										if (searchField.getText().equals(null)) {

										} else {
											list.getItems().clear();
											for (Beverage a : beverages) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}

										}
									}
								});

								sortMenu.getSelectionModel().selectedItemProperty().addListener((options, ov, nv) -> {
									if (nv.equals("sort by name (decending)")) {
										Collections.sort(beverages, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getName().charAt(0) > rhs.getName().charAt(0) ? -1
														: (lhs.getName().charAt(0) < rhs.getName().charAt(0)) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Beverage a : beverages) {
												list.getItems().add(a);
											}
										} else {
											for (Beverage a : beverages) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by calories (decending)")) {
										Collections.sort(beverages, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getCal() > rhs.getCal() ? -1
														: (lhs.getCal() < rhs.getCal()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Beverage a : beverages) {
												list.getItems().add(a);
											}
										} else {
											for (Beverage a : beverages) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by proteins (decending)")) {
										Collections.sort(beverages, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getProtein() > rhs.getProtein() ? -1
														: (lhs.getProtein() < rhs.getProtein()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Beverage a : beverages) {
												list.getItems().add(a);
											}
										} else {
											for (Beverage a : beverages) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by name (ascending)")) {
										Collections.sort(beverages, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getName().charAt(0) < rhs.getName().charAt(0) ? -1
														: (lhs.getName().charAt(0) > rhs.getName().charAt(0)) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Beverage a : beverages) {
												list.getItems().add(a);
											}
										} else {
											for (Beverage a : beverages) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by calories (ascending)")) {
										Collections.sort(beverages, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getCal() < rhs.getCal() ? -1
														: (lhs.getCal() > rhs.getCal()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Beverage a : beverages) {
												list.getItems().add(a);
											}
										} else {
											for (Beverage a : beverages) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by proteins (ascending)")) {
										Collections.sort(beverages, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getProtein() < rhs.getProtein() ? -1
														: (lhs.getProtein() > rhs.getProtein()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Beverage a : beverages) {
												list.getItems().add(a);
											}
										} else {
											for (Beverage a : beverages) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									}
								});

							} else {
								list.getItems().clear();
								for (Edible a : edibles) {
									list.getItems().add(a);
								}
								search.setOnAction(new EventHandler<ActionEvent>() {

									public void handle(ActionEvent event) {
										if (searchField.getText().equals(null)) {
										} else {
											list.getItems().clear();
											for (Edible a : edibles) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}

										}
									}
								});
								sortMenu.getSelectionModel().selectedItemProperty().addListener((options, ov, nv) -> {
									if (nv.equals("sort by name (decending)")) {
										Collections.sort(edibles, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getName().charAt(0) > rhs.getName().charAt(0) ? -1
														: (lhs.getName().charAt(0) < rhs.getName().charAt(0)) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Edible a : edibles) {
												list.getItems().add(a);
											}
										} else {
											for (Edible a : edibles) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by calories (decending)")) {
										Collections.sort(edibles, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getCal() > rhs.getCal() ? -1
														: (lhs.getCal() < rhs.getCal()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Edible a : edibles) {
												list.getItems().add(a);
											}
										} else {
											for (Edible a : edibles) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by proteins (decending)")) {
										Collections.sort(edibles, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getProtein() > rhs.getProtein() ? -1
														: (lhs.getProtein() < rhs.getProtein()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Edible a : edibles) {
												list.getItems().add(a);
											}
										} else {
											for (Edible a : edibles) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by name (ascending)")) {
										Collections.sort(edibles, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getName().charAt(0) < rhs.getName().charAt(0) ? -1
														: (lhs.getName().charAt(0) > rhs.getName().charAt(0)) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Edible a : edibles) {
												list.getItems().add(a);
											}
										} else {
											for (Edible a : edibles) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by calories (ascending)")) {
										Collections.sort(edibles, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getCal() < rhs.getCal() ? -1
														: (lhs.getCal() > rhs.getCal()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Edible a : edibles) {
												list.getItems().add(a);
											}
										} else {
											for (Edible a : edibles) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									} else if (nv.equals("sort by proteins (ascending)")) {
										Collections.sort(edibles, new Comparator<Edible>() {
											public int compare(Edible lhs, Edible rhs) {
												return lhs.getProtein() < rhs.getProtein() ? -1
														: (lhs.getProtein() > rhs.getProtein()) ? 1 : 0;
											}
										});
										list.getItems().clear();
										if (searchField.getText().equals(null)) {
											for (Edible a : edibles) {
												list.getItems().add(a);
											}
										} else {
											for (Edible a : edibles) {
												if (a.getName().toLowerCase()
														.contains(searchField.getText().toLowerCase()))
													list.getItems().add(a);
											}
										}
									}

								});

							}

						}
					}
				});

				list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Edible>() {
					public void changed(ObservableValue<? extends Edible> ov, final Edible oldValue,
							final Edible newValue) {
						try {
							if (mealNumber == 1) {
								meals[0].makeMeal(newValue, days);
								meals[0].check();
							} else if (mealNumber == 2) {
								meals[1].makeMeal(newValue, days);
								meals[1].check();
							} else if (mealNumber == 3) {
								meals[2].makeMeal(newValue, days);
								meals[2].check();
								;
							}
						} catch (tooFatException e) {
							System.out.println(e.getMessage());
						} catch (NullPointerException e) {
						}
						try {
							if (group.getSelectedToggle().equals(food)) {
								selection.getItems().clear();
								for (int i = 0; i < meals[mealNumber - 1].getMeals().get(days).size(); i++) {
									selection.getItems().add(meals[mealNumber - 1].getMeals().get(days).get(i));
								}
							} else if (group.getSelectedToggle().equals(beverage)) {
								selection.getItems().clear();
								for (int i = 0; i < meals[mealNumber - 1].getMeals().get(days).size(); i++) {
									selection.getItems().add(meals[mealNumber - 1].getMeals().get(days).get(i));
								}
							} else {
								selection.getItems().clear();
								for (int i = 0; i < meals[mealNumber - 1].getMeals().get(days).size(); i++) {
									selection.getItems().add(meals[mealNumber - 1].getMeals().get(days).get(i));
								}
							}
						} catch (NullPointerException e) {
						} catch (ArrayIndexOutOfBoundsException e) {
						}
					}
				});

				clearAll.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						selection.getItems().clear();
						if (mealNumber == 1) {
							meals[0].Clear();
						} else if (mealNumber == 2) {
							meals[1].Clear();
						} else if (mealNumber == 3)
							meals[2].Clear();

					}
				});

				nextMeal.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						selection.getItems().clear();
						if (mealNumber < 3) {
							mealNumber++;
							if (mealNumber == 2) {
								try {
									pw = new PrintWriter(new FileOutputStream("edibleSaveData.txt", true));
									pw.println("date: " + day + "/" + month + "/" + year);
									pw.close();
								} catch (FileNotFoundException e) {
								}
								nextMeal.setText("Pick Lunch");
							} else if (mealNumber == 3) {
								nextMeal.setText("Pick Dinner");
							}
							try {
								makeFile(mealNumber - 2);
							} catch (FileNotFoundException e) {
							}
						} else if (mealNumber == 3) {
							mealNumber++;
							nextMeal.setText("Enter the next day");
							try {
								makeFile(mealNumber - 2);
							} catch (FileNotFoundException e) {
							}
						} else if (mealNumber > 3) {
							day++;
							days++;
							if (day > 31) {
								day = 01;
								month++;
							}
							if (month > 12) {
								day = 01;
								month = 01;
								year++;
							}
							mealNumber = 1;
							nextMeal.setText("Pick Breakfast");
							date.setText("Date: " + day + "/" + month + "/" + year);
						}

					}

				});

			}

		});

		progress.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				double baverageSize = 0, calF = 0, protF = 0, fatsF = 0, carbF = 0;// NUTRITION DETAILS OF THE EDIBLES
				ArrayList<Double> beverageChart = new ArrayList<>(), calChart = new ArrayList<>(),
						protChart = new ArrayList<>(), fatChart = new ArrayList<>(), carbChart = new ArrayList<>();
				double size = 0;
				String unit;
				ProgressBar calories = new ProgressBar(0);// 2000cal
				ProgressBar protein = new ProgressBar(0);// 100g
				ProgressBar fat = new ProgressBar(0);// 50g
				ProgressBar carbohydrates = new ProgressBar(0);// 300g
				ProgressBar beverages = new ProgressBar(0);// 3000ml

				Button viewProtChart = new Button("View monthly chart of consumed proteins");
				Button viewFatChart = new Button("View monthly chart of consumed fats");
				Button viewCalChart = new Button("View monthly chart of consumed Calories");
				Button viewCarbChart = new Button("View monthly chart of consumed Carbohydrates");
				Button viewBevChart = new Button("View monthly chart of consumed Beverages");

				ProgressIndicator bar1 = new ProgressIndicator();
				ProgressIndicator bar2 = new ProgressIndicator();
				ProgressIndicator bar3 = new ProgressIndicator();
				ProgressIndicator bar4 = new ProgressIndicator();
				ProgressIndicator bar5 = new ProgressIndicator();

				Label carbsLabel = new Label("Carbs (out of 300g)");
				Label fatLabel = new Label("Fats (out of 50g)");
				Label protLabel = new Label("Protein (out of 100g)");
				Label calLabel = new Label("Calories(out of 2000cal)");
				Label bavLabel = new Label("Beverages (out of 3000ml)");
				Stage newWindow = new Stage();
				VBox vbox = new VBox();
				Scene scene2 = new Scene(vbox, 450, 650);
				newWindow.setTitle("progress so far");
				newWindow.setScene(scene2);
				newWindow.show();

				int i = 0;

				try {
					Scanner input = new Scanner(new FileInputStream("edibleSaveData.txt"));
					while (input.hasNext()) {

						if (input.next().contains("date")) {
							calChart.add(i, calF);
							fatChart.add(i, fatsF);
							carbChart.add(i, carbF);
							protChart.add(i, protF);
							beverageChart.add(i, baverageSize);
							i++;
							input.nextLine();
							calF = 0;
							fatsF = 0;
							carbF = 0;
							protF = 0;
							baverageSize = 0;
							if (i > 30) {
								i = 0;
								calChart.clear();
								fatChart.clear();
								carbChart.clear();
								protChart.clear();
								beverageChart.clear();
							}
						}
						input.nextLine();

						size = input.nextDouble();
						unit = input.next();

						calF += input.nextDouble();
						fatsF += input.nextDouble();
						carbF += input.nextDouble();
						protF += input.nextDouble();

						if (unit.equals("ml")) {
							baverageSize += size;
						}

					}
					if (i < 31) {
						calChart.add(i, calF);
						fatChart.add(i, fatsF);
						carbChart.add(i, carbF);
						protChart.add(i, protF);
						beverageChart.add(i, baverageSize);
					} else {
						calChart.add(0, calF);
						fatChart.add(0, fatsF);
						carbChart.add(0, carbF);
						protChart.add(0, protF);
						beverageChart.add(0, baverageSize);
					}

					calories.setProgress(calF / 2000.0);
					protein.setProgress(protF / 100.0);
					fat.setProgress(fatsF / 50.0);
					carbohydrates.setProgress(carbF / 300.0);
					beverages.setProgress(baverageSize / 3000.0);
					bar1.setProgress(calF / 2000.0);
					bar2.setProgress(protF / 100.0);
					bar3.setProgress(fatsF / 50.0);
					bar4.setProgress(carbF / 300.0);
					bar5.setProgress(baverageSize / 3000.0);

					vbox.getChildren().add(new Label("Daily Summery:"));
					vbox.getChildren().add(new Label(""));
					vbox.getChildren().addAll(carbsLabel, carbohydrates, bar1, viewCarbChart);
					vbox.getChildren().add(new Label(""));
					vbox.getChildren().addAll(protLabel, protein, bar2, viewProtChart);
					vbox.getChildren().add(new Label(""));
					vbox.getChildren().addAll(fatLabel, fat, bar3, viewFatChart);
					vbox.getChildren().add(new Label(""));
					vbox.getChildren().addAll(calLabel, calories, bar4, viewCalChart);
					vbox.getChildren().add(new Label(""));
					vbox.getChildren().addAll(bavLabel, beverages, bar5, viewBevChart);

					viewProtChart.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							Stage newWindow2 = new Stage();
							VBox chart = new VBox();

							NumberAxis xAxis = new NumberAxis();
							xAxis.setLabel("Days");
							NumberAxis yAxis = new NumberAxis();
							yAxis.setLabel("Proteins (g)");

							LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

							XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

							for (int j = 0; j < protChart.size(); j++)
								series.getData().add(new XYChart.Data(j, protChart.get(j)));

							lineChart.getData().add(series);
							chart.getChildren().add(lineChart);

							Scene scene3 = new Scene(chart, 400, 400);
							newWindow2.setTitle("Protein monthly chart");
							newWindow2.setScene(scene3);
							newWindow2.show();
						}
					});

					viewFatChart.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							Stage newWindow2 = new Stage();
							VBox chart = new VBox();

							NumberAxis xAxis = new NumberAxis();
							xAxis.setLabel("Days");
							NumberAxis yAxis = new NumberAxis();
							yAxis.setLabel("Fat (g)");

							LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

							XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

							for (int j = 0; j < fatChart.size(); j++)
								series.getData().add(new XYChart.Data(j, fatChart.get(j)));

							lineChart.getData().add(series);
							chart.getChildren().add(lineChart);

							Scene scene3 = new Scene(chart, 400, 400);
							newWindow2.setTitle("Fat monthly chart");
							newWindow2.setScene(scene3);
							newWindow2.show();
						}
					});

					viewCalChart.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							Stage newWindow2 = new Stage();
							VBox chart = new VBox();

							NumberAxis xAxis = new NumberAxis();
							xAxis.setLabel("Days");
							NumberAxis yAxis = new NumberAxis();
							yAxis.setLabel("Calories (cal)");

							LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

							XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

							for (int j = 0; j < calChart.size(); j++)
								series.getData().add(new XYChart.Data(j, calChart.get(j)));

							lineChart.getData().add(series);
							chart.getChildren().add(lineChart);

							Scene scene3 = new Scene(chart, 400, 400);
							newWindow2.setTitle("Calories monthly chart");
							newWindow2.setScene(scene3);
							newWindow2.show();
						}
					});

					viewCarbChart.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							Stage newWindow2 = new Stage();
							VBox chart = new VBox();

							NumberAxis xAxis = new NumberAxis();
							xAxis.setLabel("Days");
							NumberAxis yAxis = new NumberAxis();
							yAxis.setLabel("Carbohydrates (g)");

							LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

							XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

							for (int j = 0; j < carbChart.size(); j++)
								series.getData().add(new XYChart.Data(j, carbChart.get(j)));

							lineChart.getData().add(series);
							chart.getChildren().add(lineChart);

							Scene scene3 = new Scene(chart, 400, 400);
							newWindow2.setTitle("Carbohydrates monthly chart");
							newWindow2.setScene(scene3);
							newWindow2.show();
						}
					});

					viewBevChart.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							Stage newWindow2 = new Stage();
							VBox chart = new VBox();

							NumberAxis xAxis = new NumberAxis();
							xAxis.setLabel("Days");
							NumberAxis yAxis = new NumberAxis();
							yAxis.setLabel("Beverages(ml)");

							LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

							XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

							for (int j = 0; j < beverageChart.size(); j++)
								series.getData().add(new XYChart.Data(j, beverageChart.get(j)));

							lineChart.getData().add(series);
							chart.getChildren().add(lineChart);

							Scene scene3 = new Scene(chart, 400, 400);
							newWindow2.setTitle("Beverages monthly chart");
							newWindow2.setScene(scene3);
							newWindow2.show();
						}
					});

				} catch (FileNotFoundException e) {
					System.out.println("file was not found");
				} catch (InputMismatchException e1) {
					System.out.println("input mismatch");
				}

			}

		});

	}
	// end of gui
}