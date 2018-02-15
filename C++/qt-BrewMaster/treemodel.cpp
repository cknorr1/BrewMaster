#include <QtWidgets>

#include "treeitem.h"
#include "treemodel.h"
#include <iostream>

TreeModel::TreeModel(const QStringList &headers, const QString &data, QObject *parent)
	: QAbstractItemModel(parent)
{
	QVector<QVariant> rootData;
	foreach (QString header, headers)
		rootData << header;

	rootItem = new TreeItem(rootData);
	setupModelData(data.split(QString("\n")), rootItem);
}

TreeModel::~TreeModel()
{
	delete rootItem;
}

int TreeModel::columnCount(const QModelIndex & /* parent */) const
{
	return rootItem->columnCount();
}

QVariant TreeModel::data(const QModelIndex &index, int role) const
{
	if (!index.isValid())
		return QVariant();

	if (role != Qt::DisplayRole && role != Qt::EditRole)
		return QVariant();

	TreeItem *item = getItem(index);

	return item->data(index.column());
}

Qt::ItemFlags TreeModel::flags(const QModelIndex &index) const
{
	if (!index.isValid())
		return 0;

	return Qt::ItemIsEditable | QAbstractItemModel::flags(index);
}

TreeItem *TreeModel::getItem(const QModelIndex &index) const
{
	if (index.isValid()) {
		TreeItem *item = static_cast<TreeItem*>(index.internalPointer());
		if (item)
			return item;
	}
	return rootItem;
}

QVariant TreeModel::headerData(int section, Qt::Orientation orientation,
							int role) const
{
	if (orientation == Qt::Horizontal && role == Qt::DisplayRole)
		return rootItem->data(section);

	return QVariant();
}

QModelIndex TreeModel::index(int row, int column, const QModelIndex &parent) const
{
	if (parent.isValid() && parent.column() != 0)
		return QModelIndex();

	TreeItem *parentItem = getItem(parent);

	TreeItem *childItem = parentItem->child(row);
	if (childItem)
		return createIndex(row, column, childItem);
	else
		return QModelIndex();
}

bool TreeModel::insertColumns(int position, int columns, const QModelIndex &parent)
{
	bool success;

	beginInsertColumns(parent, position, position + columns - 1);
	success = rootItem->insertColumns(position, columns);
	endInsertColumns();

	return success;
}

bool TreeModel::insertRows(int position, int rows, const QModelIndex &parent)
{
	TreeItem *parentItem = getItem(parent);
	bool success;

	beginInsertRows(parent, position, position + rows - 1);
	success = parentItem->insertChildren(position, rows, rootItem->columnCount());
	endInsertRows();

	return success;
}

QModelIndex TreeModel::parent(const QModelIndex &index) const
{
	if (!index.isValid())
		return QModelIndex();

	TreeItem *childItem = getItem(index);
	TreeItem *parentItem = childItem->parent();

	if (parentItem == rootItem)
		return QModelIndex();

	return createIndex(parentItem->childNumber(), 0, parentItem);
}

bool TreeModel::removeColumns(int position, int columns, const QModelIndex &parent)
{
	bool success;

	beginRemoveColumns(parent, position, position + columns - 1);
	success = rootItem->removeColumns(position, columns);
	endRemoveColumns();

	if (rootItem->columnCount() == 0)
		removeRows(0, rowCount());

	return success;
}

bool TreeModel::removeRows(int position, int rows, const QModelIndex &parent)
{
	TreeItem *parentItem = getItem(parent);
	bool success = true;

	beginRemoveRows(parent, position, position + rows - 1);
	success = parentItem->removeChildren(position, rows);
	endRemoveRows();

	return success;
}

int TreeModel::rowCount(const QModelIndex &parent) const
{
	TreeItem *parentItem = getItem(parent);

	return parentItem->childCount();
}

bool TreeModel::setData(const QModelIndex &index, const QVariant &value, int role)
{
	if (role != Qt::EditRole)
		return false;

	TreeItem *item = getItem(index);
	bool result = item->setData(index.column(), value);

	if (result)
		emit dataChanged(index, index);

	return result;
}

void TreeModel::addBrewStep ( const QString& name, float temp, float duration, float stirSpeed )
{
			beginInsertRows(QModelIndex(), rootItem->childCount(), rootItem->childCount() + 1);
			rootItem->insertChildren(rootItem->childCount(), 1, rootItem->columnCount());
			TreeItem *brewStep = rootItem->child(rootItem->childCount() - 1);
			brewStep->setData(0, name);
			brewStep->insertChildren(brewStep->childCount(), 1, rootItem->columnCount());
			brewStep->child(brewStep->childCount() - 1)->setData(0,"Temperatur [°C]");
			brewStep->child(brewStep->childCount() - 1)->setData(1,temp);
			brewStep->insertChildren(brewStep->childCount(), 1, rootItem->columnCount());
			brewStep->child(brewStep->childCount() - 1)->setData(0,"Dauer [min]");
			brewStep->child(brewStep->childCount() - 1)->setData(2,"-1: Bis Temperatur erreicht, 0: Unendlich");
			brewStep->child(brewStep->childCount() - 1)->setData(1,duration);
			brewStep->insertChildren(brewStep->childCount(), 1, rootItem->columnCount());
			brewStep->child(brewStep->childCount() - 1)->setData(0,"Rührgeschwindigkeit [%]");
			brewStep->child(brewStep->childCount() - 1)->setData(1,stirSpeed);
			endInsertRows();
}

void TreeModel::addEmptyBrewStep () {
			beginInsertRows(QModelIndex(), rootItem->childCount(), rootItem->childCount() + 1);
			rootItem->insertChildren(rootItem->childCount(), 1, rootItem->columnCount());
			TreeItem *brewStep = rootItem->child(rootItem->childCount() - 1);
			brewStep->setData(0, QString(QString::number(rootItem->childCount()) + ". Schritt"));
			brewStep->insertChildren(brewStep->childCount(), 1, rootItem->columnCount());
			brewStep->child(brewStep->childCount() - 1)->setData(0,"Temperatur [°C]");
			brewStep->insertChildren(brewStep->childCount(), 1, rootItem->columnCount());
			brewStep->child(brewStep->childCount() - 1)->setData(0,"Dauer [min]");
			brewStep->child(brewStep->childCount() - 1)->setData(2,"-1: Bis Temperatur erreicht, 0: Unendlich");
			brewStep->insertChildren(brewStep->childCount(), 1, rootItem->columnCount());
			brewStep->child(brewStep->childCount() - 1)->setData(0,"Rührgeschwindigkeit [%]");
			endInsertRows();
}

void TreeModel::insertBrewStep ( int position, const QString& name, float temp, float duration, float stirSpeed ) {
			beginInsertRows(QModelIndex(), position, position + 1);
			rootItem->insertChildren(position, 1, rootItem->columnCount());
			TreeItem *brewStep = rootItem->child(position);
			brewStep->setData(0, name);
			brewStep->insertChildren(brewStep->childCount(), 1, rootItem->columnCount());
			brewStep->child(brewStep->childCount() - 1)->setData(0,"Temperatur [°C]");
			brewStep->child(brewStep->childCount() - 1)->setData(1,temp);
			brewStep->insertChildren(brewStep->childCount(), 1, rootItem->columnCount());
			brewStep->child(brewStep->childCount() - 1)->setData(0,"Dauer [min]");
			brewStep->child(brewStep->childCount() - 1)->setData(2,"-1: Bis Temperatur erreicht, 0: Unendlich");
			brewStep->child(brewStep->childCount() - 1)->setData(1,duration);
			brewStep->insertChildren(brewStep->childCount(), 1, rootItem->columnCount());
			brewStep->child(brewStep->childCount() - 1)->setData(0,"Rührgeschwindigkeit [%]");
			brewStep->child(brewStep->childCount() - 1)->setData(1,stirSpeed);
			endInsertRows();
}

QStringList TreeModel::getBrewStepsAsStrings() {
	QStringList brewRecipe;
	for(int i = 0; i < rootItem->childCount(); i++){
		TreeItem *brewStep = rootItem->child(i);
		if(brewStep->childCount() == 3) {
			brewRecipe.append(QString::number(i) + ";" + brewStep->data(0).toString() + ";" + brewStep->child(0)->data(1).toString() + ";" + brewStep->child(1)->data(1).toString() + ";" + brewStep->child(2)->data(1).toString());
		}
	}
	
	return brewRecipe;
}


bool TreeModel::setHeaderData(int section, Qt::Orientation orientation,
							const QVariant &value, int role)
{
	if (role != Qt::EditRole || orientation != Qt::Horizontal)
		return false;

	bool result = rootItem->setData(section, value);

	if (result)
		emit headerDataChanged(orientation, section, section);

	return result;
}

void TreeModel::setupModelData(const QStringList &lines, TreeItem *parent)
{
	QList<TreeItem*> parents;
	QList<int> indentations;
	parents << parent;
	indentations << 0;

	int number = 0;

	while (number < lines.count()) {
		int position = 0;
		while (position < lines[number].length()) {
			if (lines[number].at(position) != ' ')
				break;
			++position;
		}

		QString lineData = lines[number].mid(position).trimmed();

		if (!lineData.isEmpty()) {
			// Read the column data from the rest of the line.
			QStringList columnStrings = lineData.split("\t", QString::SkipEmptyParts);
			QVector<QVariant> columnData;
			for (int column = 0; column < columnStrings.count(); ++column)
				columnData << columnStrings[column];

			if (position > indentations.last()) {
				// The last child of the current parent is now the new parent
				// unless the current parent has no children.

				if (parents.last()->childCount() > 0) {
					parents << parents.last()->child(parents.last()->childCount()-1);
					indentations << position;
				}
			} else {
				while (position < indentations.last() && parents.count() > 0) {
					parents.pop_back();
					indentations.pop_back();
				}
			}

			// Append a new item to the current parent's list of children.
			TreeItem *parent = parents.last();
			parent->insertChildren(parent->childCount(), 1, rootItem->columnCount());
			for (int column = 0; column < columnData.size(); ++column)
				parent->child(parent->childCount() - 1)->setData(column, columnData[column]);
		}

		++number;
	}
}

void TreeModel::printModel() {
	rootItem->printTreeItem();
}
